package com.app.pokemon.service;

import com.app.pokemon.api.*;
import com.app.pokemon.api.external.PokemonBattleApiResponse;
import com.app.pokemon.api.external.PokemonMoveApiResponse;
import com.app.pokemon.client.PokemonApiClient;
import com.app.pokemon.domain.Type;
import com.app.pokemon.exception.BadRequestException;
import com.app.pokemon.exception.SvNotAvailableException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SvBattleService {

    private static final int LEVEL = 50;
    private static final int IV = 31;
    private static final int EV = 0;

    private final PokemonApiClient pokemonApiClient;
    private final MoveSelectService moveSelectService;
    private final StatCalcService statCalcService;
    private final DamageCalcService damageCalcService;
    private final SvDexService svDexService;

    public SvBattleService(
            PokemonApiClient pokemonApiClient,
            MoveSelectService moveSelectService,
            StatCalcService statCalcService,
            DamageCalcService damageCalcService
    ) {
        this.pokemonApiClient = pokemonApiClient;
        this.moveSelectService = moveSelectService;
        this.statCalcService = statCalcService;
        this.damageCalcService = damageCalcService;
        this.svDexService = new SvDexService();
    }

    /**
     * 画面用：技名 + ダメージを返す（fullBattleは使わない）
     */
    public SvBattleViewResponse svBattleForView(SvBattleRequest req) {
        validate(req);

        PokemonBattleApiResponse a = pokemonApiClient.fetchPokemonBattle(req.attackerId());
        PokemonBattleApiResponse b = pokemonApiClient.fetchPokemonBattle(req.defenderId());

        Type typeA1 = toType(a.types().get(0).type().name());
        Type typeA2 = a.types().size() >= 2 ? toType(a.types().get(1).type().name()) : null;

        Type typeB1 = toType(b.types().get(0).type().name());
        Type typeB2 = b.types().size() >= 2 ? toType(b.types().get(1).type().name()) : null;

        BaseStats baseA = extractBaseStats(a);
        BaseStats baseB = extractBaseStats(b);

        RealStats rsA = calcLv50Neutral(baseA);
        RealStats rsB = calcLv50Neutral(baseB);

        List<String> moveNamesA = moveSelectService.selectLevelUpMovesUpTo50Sv(req.attackerId());
        List<String> moveNamesB = moveSelectService.selectLevelUpMovesUpTo50Sv(req.defenderId());

        PickedMove bestA = pickBestMove(moveNamesA, typeA1, typeA2, typeB1, typeB2);
        PickedMove bestB = pickBestMove(moveNamesB, typeB1, typeB2, typeA1, typeA2);

        // A→B ダメージ
        double effAtoB = effectiveness(bestA.type(), typeB1, typeB2);
        boolean stabA = isStab(bestA.type(), typeA1, typeA2);
        int dmgAtoB = damageCalcService.calc(new DamageCalcRequest(
                LEVEL,
                Math.max(bestA.power(), 1),
                rsA.attack(),
                rsB.defense(),
                stabA,
                effAtoB
        )).damage();

        // B→A ダメージ
        double effBtoA = effectiveness(bestB.type(), typeA1, typeA2);
        boolean stabB = isStab(bestB.type(), typeB1, typeB2);
        int dmgBtoA = damageCalcService.calc(new DamageCalcRequest(
                LEVEL,
                Math.max(bestB.power(), 1),
                rsB.attack(),
                rsA.defense(),
                stabB,
                effBtoA
        )).damage();

        return new SvBattleViewResponse(
                req.attackerId(),
                req.defenderId(),
                bestA.name(),
                bestB.name(),
                dmgAtoB,
                dmgBtoA
        );
    }

    private void validate(SvBattleRequest req) {
        if (req == null) throw new BadRequestException("request must not be null");
        if (req.attackerId() <= 0 || req.defenderId() <= 0) {
            throw new BadRequestException("pokemonId must be > 0");
        }
    }

    // ---- 技の自動選択 ----

    private PickedMove pickBestMove(List<String> moveNames,
                                    Type atk1, Type atk2,
                                    Type def1, Type def2) {
        if (moveNames == null || moveNames.isEmpty()) {
            return new PickedMove("struggle", 50, Type.NORMAL);
        }

        return moveNames.stream()
                .map(this::fetchMoveOrNull)
                .filter(Objects::nonNull)
                .filter(m -> m.power() > 0)
                .max(Comparator.comparingDouble(m -> score(m, atk1, atk2, def1, def2)))
                .orElseGet(() -> new PickedMove("struggle", 50, Type.NORMAL));
    }

    private double score(PickedMove m, Type atk1, Type atk2, Type def1, Type def2) {
        double eff = effectiveness(m.type(), def1, def2);
        double stab = isStab(m.type(), atk1, atk2) ? 1.5 : 1.0;
        return m.power() * eff * stab;
    }

    private PickedMove fetchMoveOrNull(String moveName) {
        if (moveName == null || moveName.isBlank()) return null;

        PokemonMoveApiResponse res = pokemonApiClient.fetchMove(moveName);
        if (res == null || res.type() == null || res.type().name() == null) return null;

        int power = (res.power() == null) ? 0 : res.power();
        Type type = toType(res.type().name());
        return new PickedMove(moveName, power, type);
    }

    private boolean isStab(Type moveType, Type type1, Type type2) {
        if (moveType == null) return false;
        if (moveType == type1) return true;
        return type2 != null && moveType == type2;
    }

    // ---- 種族値 -> 実数値 ----

    private RealStats calcLv50Neutral(BaseStats base) {
        int hp = statCalcService.calcHp(base.hp(), IV, EV, LEVEL);
        int atk = statCalcService.calcStat(base.attack(), IV, EV, LEVEL);
        int def = statCalcService.calcStat(base.defense(), IV, EV, LEVEL);
        int spd = statCalcService.calcStat(base.speed(), IV, EV, LEVEL);
        return new RealStats(hp, atk, def, spd);
    }

    private BaseStats extractBaseStats(PokemonBattleApiResponse r) {
        int hp = findBaseStat(r, "hp");
        int attack = findBaseStat(r, "attack");
        int defense = findBaseStat(r, "defense");
        int speed = findBaseStat(r, "speed");
        return new BaseStats(hp, attack, defense, speed);
    }

    private int findBaseStat(PokemonBattleApiResponse r, String statName) {
        return r.stats().stream()
                .filter(s -> s != null && s.stat() != null && statName.equals(s.stat().name()))
                .findFirst()
                .map(PokemonBattleApiResponse.StatSlot::base_stat)
                .orElseThrow(() -> new RuntimeException("base_stat not found: " + statName));
    }

    private Type toType(String pokeApiTypeName) {
        return Type.valueOf(pokeApiTypeName.toUpperCase());
    }

    // ---- 相性（18タイプ完全版）----

    private static final EnumMap<Type, EnumSet<Type>> SUPER = new EnumMap<>(Type.class);
    private static final EnumMap<Type, EnumSet<Type>> RESIST = new EnumMap<>(Type.class);
    private static final EnumMap<Type, EnumSet<Type>> IMMUNE = new EnumMap<>(Type.class);

    static {
        // SUPER（こうかは ばつぐんだ）
        SUPER.put(Type.NORMAL, EnumSet.noneOf(Type.class));
        SUPER.put(Type.FIRE, EnumSet.of(Type.GRASS, Type.ICE, Type.BUG, Type.STEEL));
        SUPER.put(Type.WATER, EnumSet.of(Type.FIRE, Type.GROUND, Type.ROCK));
        SUPER.put(Type.ELECTRIC, EnumSet.of(Type.WATER, Type.FLYING));
        SUPER.put(Type.GRASS, EnumSet.of(Type.WATER, Type.GROUND, Type.ROCK));
        SUPER.put(Type.ICE, EnumSet.of(Type.GRASS, Type.GROUND, Type.FLYING, Type.DRAGON));
        SUPER.put(Type.FIGHTING, EnumSet.of(Type.NORMAL, Type.ICE, Type.ROCK, Type.DARK, Type.STEEL));
        SUPER.put(Type.POISON, EnumSet.of(Type.GRASS, Type.FAIRY));
        SUPER.put(Type.GROUND, EnumSet.of(Type.FIRE, Type.ELECTRIC, Type.POISON, Type.ROCK, Type.STEEL));
        SUPER.put(Type.FLYING, EnumSet.of(Type.GRASS, Type.FIGHTING, Type.BUG));
        SUPER.put(Type.PSYCHIC, EnumSet.of(Type.FIGHTING, Type.POISON));
        SUPER.put(Type.BUG, EnumSet.of(Type.GRASS, Type.PSYCHIC, Type.DARK));
        SUPER.put(Type.ROCK, EnumSet.of(Type.FIRE, Type.ICE, Type.FLYING, Type.BUG));
        SUPER.put(Type.GHOST, EnumSet.of(Type.PSYCHIC, Type.GHOST));
        SUPER.put(Type.DRAGON, EnumSet.of(Type.DRAGON));
        SUPER.put(Type.DARK, EnumSet.of(Type.PSYCHIC, Type.GHOST));
        SUPER.put(Type.STEEL, EnumSet.of(Type.ICE, Type.ROCK, Type.FAIRY));
        SUPER.put(Type.FAIRY, EnumSet.of(Type.FIGHTING, Type.DRAGON, Type.DARK));

        // RESIST（こうかは いまひとつだ）
        RESIST.put(Type.NORMAL, EnumSet.of(Type.ROCK, Type.STEEL));
        RESIST.put(Type.FIRE, EnumSet.of(Type.FIRE, Type.WATER, Type.ROCK, Type.DRAGON));
        RESIST.put(Type.WATER, EnumSet.of(Type.WATER, Type.GRASS, Type.DRAGON));
        RESIST.put(Type.ELECTRIC, EnumSet.of(Type.ELECTRIC, Type.GRASS, Type.DRAGON));
        RESIST.put(Type.GRASS, EnumSet.of(Type.FIRE, Type.GRASS, Type.POISON, Type.FLYING, Type.BUG, Type.DRAGON, Type.STEEL));
        RESIST.put(Type.ICE, EnumSet.of(Type.FIRE, Type.WATER, Type.ICE, Type.STEEL));
        RESIST.put(Type.FIGHTING, EnumSet.of(Type.POISON, Type.FLYING, Type.PSYCHIC, Type.BUG, Type.FAIRY));
        RESIST.put(Type.POISON, EnumSet.of(Type.POISON, Type.GROUND, Type.ROCK, Type.GHOST));
        RESIST.put(Type.GROUND, EnumSet.of(Type.GRASS, Type.BUG));
        RESIST.put(Type.FLYING, EnumSet.of(Type.ELECTRIC, Type.ROCK, Type.STEEL));
        RESIST.put(Type.PSYCHIC, EnumSet.of(Type.PSYCHIC, Type.STEEL));
        RESIST.put(Type.BUG, EnumSet.of(Type.FIRE, Type.FIGHTING, Type.POISON, Type.FLYING, Type.GHOST, Type.STEEL, Type.FAIRY));
        RESIST.put(Type.ROCK, EnumSet.of(Type.FIGHTING, Type.GROUND, Type.STEEL));
        RESIST.put(Type.GHOST, EnumSet.of(Type.DARK));
        RESIST.put(Type.DRAGON, EnumSet.of(Type.STEEL));
        RESIST.put(Type.DARK, EnumSet.of(Type.FIGHTING, Type.DARK, Type.FAIRY));
        RESIST.put(Type.STEEL, EnumSet.of(Type.FIRE, Type.WATER, Type.ELECTRIC, Type.STEEL));
        RESIST.put(Type.FAIRY, EnumSet.of(Type.FIRE, Type.POISON, Type.STEEL));

        // IMMUNE（こうかが ない）
        IMMUNE.put(Type.NORMAL, EnumSet.of(Type.GHOST));
        IMMUNE.put(Type.FIGHTING, EnumSet.of(Type.GHOST));
        IMMUNE.put(Type.POISON, EnumSet.of(Type.STEEL));
        IMMUNE.put(Type.GROUND, EnumSet.of(Type.FLYING));
        IMMUNE.put(Type.PSYCHIC, EnumSet.of(Type.DARK));
        IMMUNE.put(Type.GHOST, EnumSet.of(Type.NORMAL));
        IMMUNE.put(Type.ELECTRIC, EnumSet.of(Type.GROUND));
        IMMUNE.put(Type.DRAGON, EnumSet.of(Type.FAIRY));
        // その他は免疫なし
        IMMUNE.putIfAbsent(Type.FIRE, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.WATER, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.GRASS, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.ICE, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.FLYING, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.BUG, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.ROCK, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.DARK, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.STEEL, EnumSet.noneOf(Type.class));
        IMMUNE.putIfAbsent(Type.FAIRY, EnumSet.noneOf(Type.class));
    }

    private double effectiveness(Type atk, Type def1, Type def2) {
        double m1 = singleEffectiveness(atk, def1);
        double m2 = (def2 == null) ? 1.0 : singleEffectiveness(atk, def2);
        return m1 * m2;
    }

    private double singleEffectiveness(Type atk, Type def) {
        if (atk == null || def == null) return 1.0;

        if (IMMUNE.getOrDefault(atk, EnumSet.noneOf(Type.class)).contains(def)) return 0.0;
        if (SUPER.getOrDefault(atk, EnumSet.noneOf(Type.class)).contains(def)) return 2.0;
        if (RESIST.getOrDefault(atk, EnumSet.noneOf(Type.class)).contains(def)) return 0.5;
        return 1.0;
    }

    /**
     * 画面の「図鑑番号2つ入力」から、勝敗決定まで（fullBattle）を回すための入口。
     * - pokeAPIからA/B取得
     * - 実数値(Lv50無補正)算出
     * - Lv50までの技から最良技を自動選択
     * - FullBattleRequest を組み立てて fullBattle() を呼ぶ
     */
    public FullBattleResponse fullBattleByIds(int attackerId, int defenderId) {
        if (attackerId <= 0 || defenderId <= 0) {
            throw new BadRequestException("pokemonId must be > 0");
        }

        PokemonBattleApiResponse a = pokemonApiClient.fetchPokemonBattle(attackerId);
        PokemonBattleApiResponse b = pokemonApiClient.fetchPokemonBattle(defenderId);

        Type typeA1 = toType(a.types().get(0).type().name());
        Type typeA2 = a.types().size() >= 2 ? toType(a.types().get(1).type().name()) : null;

        Type typeB1 = toType(b.types().get(0).type().name());
        Type typeB2 = b.types().size() >= 2 ? toType(b.types().get(1).type().name()) : null;

        BaseStats baseA = extractBaseStats(a);
        BaseStats baseB = extractBaseStats(b);

        RealStats rsA = calcLv50Neutral(baseA);
        RealStats rsB = calcLv50Neutral(baseB);

        List<String> moveNamesA = moveSelectService.selectLevelUpMovesUpTo50Sv(attackerId);
        List<String> moveNamesB = moveSelectService.selectLevelUpMovesUpTo50Sv(defenderId);

        PickedMove bestA = pickBestMove(moveNamesA, typeA1, typeA2, typeB1, typeB2);
        PickedMove bestB = pickBestMove(moveNamesB, typeB1, typeB2, typeA1, typeA2);

        boolean stabA = isStab(bestA.type(), typeA1, typeA2);
        boolean stabB = isStab(bestB.type(), typeB1, typeB2);

        // FullBattle は「実数値直渡し」なのでここで request を組み立てる
        FullBattleRequest fullReq = new FullBattleRequest(
                rsA.hp(),
                rsA.attack(),
                rsA.defense(),
                rsA.speed(),
                Math.max(bestA.power(), 1),
                bestA.type(),
                stabA,
                typeA1,
                typeA2,

                rsB.hp(),
                rsB.attack(),
                rsB.defense(),
                rsB.speed(),
                Math.max(bestB.power(), 1),
                bestB.type(),
                stabB,
                typeB1,
                typeB2
        );

        return fullBattle(fullReq);
    }



    /**
     * 勝敗決定まで回すための最小入力（実数値を直接渡す）
     * Lv50固定・乱数あり（同速のみ1/2）
     * <p>
     * FullBattleRequest:
     * - A/B の HP/攻撃/防御/素早さ/技威力/技タイプ/STAB/防御側タイプ
     */
    public FullBattleResponse fullBattle(FullBattleRequest req) {
        if (req == null) throw new BadRequestException("request must not be null");

        int hpA = req.hpA();
        int hpB = req.hpB();

        String first = decideFirst(req.speedA(), req.speedB());

        List<BattleTurnLog> logs = new ArrayList<>();
        int turn = 0;

        final int MAX_TURNS = 100; // 念のため無限ループ防止（Struggle等で1ダメ保証してても保険）

        while (hpA > 0 && hpB > 0 && turn < MAX_TURNS) {
            turn++;

            if ("A".equals(first)) {
                // A -> B
                int dmgA = calcDamageByServices(
                        req.attackA(), req.defenseB(),
                        req.powerA(), req.moveTypeA(),
                        req.stabA(),
                        req.typeB1(), req.typeB2()
                );
                hpB = Math.max(0, hpB - dmgA);
                logs.add(new BattleTurnLog(turn, "A", dmgA, hpA, hpB));
                if (hpB <= 0) break;

                // B -> A
                int dmgB = calcDamageByServices(
                        req.attackB(), req.defenseA(),
                        req.powerB(), req.moveTypeB(),
                        req.stabB(),
                        req.typeA1(), req.typeA2()
                );
                hpA = Math.max(0, hpA - dmgB);
                logs.add(new BattleTurnLog(turn, "B", dmgB, hpA, hpB));

            } else {
                // B -> A
                int dmgB = calcDamageByServices(
                        req.attackB(), req.defenseA(),
                        req.powerB(), req.moveTypeB(),
                        req.stabB(),
                        req.typeA1(), req.typeA2()
                );
                hpA = Math.max(0, hpA - dmgB);
                logs.add(new BattleTurnLog(turn, "B", dmgB, hpA, hpB));
                if (hpA <= 0) break;

                // A -> B
                int dmgA = calcDamageByServices(
                        req.attackA(), req.defenseB(),
                        req.powerA(), req.moveTypeA(),
                        req.stabA(),
                        req.typeB1(), req.typeB2()
                );
                hpB = Math.max(0, hpB - dmgA);
                logs.add(new BattleTurnLog(turn, "A", dmgA, hpA, hpB));
            }
        }

        String winner = decideWinner(hpA, hpB);

        return new FullBattleResponse(
                first,
                turn,
                hpA,
                hpB,
                winner,
                logs
        );
    }

    private String decideFirst(int speedA, int speedB) {
        if (speedA > speedB) return "A";
        if (speedB > speedA) return "B";
        // 同速のみ1/2
        return ThreadLocalRandom.current().nextBoolean() ? "A" : "B";
    }

    private String decideWinner(int hpA, int hpB) {
        if (hpA <= 0 && hpB <= 0) return "DRAW";
        if (hpA <= 0) return "B";
        if (hpB <= 0) return "A";
        return "DRAW";
    }

    /**
     * 既存資産（effectiveness + damageCalcService）を使って最終ダメージを出す。
     * - power は 0 だと詰むので最低 1
     * - 乱数は damageCalcService 側に含まれている前提
     */
    private int calcDamageByServices(
            int atk,
            int def,
            int power,
            Type moveType,
            boolean stab,
            Type defenderType1,
            Type defenderType2
    ) {
        double eff = effectiveness(moveType, defenderType1, defenderType2);

        int p = Math.max(power, 1);

        int damage = damageCalcService.calc(new DamageCalcRequest(
                LEVEL,
                p,
                atk,
                def,
                stab,
                eff
        )).damage();

        // 最低1（ダメ計サービスが0を返す可能性があるなら保険）
        return Math.max(1, damage);
    }

    // 既存クラスに追加
    private void validateSvAvailable(int id) {
        if (!svDexService.isInSv(id)) {
            throw new SvNotAvailableException("SVに登場しないポケモンです（図鑑番号: " + id + "）");
        }
    }



    // ---- 内部record ----
    private record BaseStats(int hp, int attack, int defense, int speed) {
    }

    private record RealStats(int hp, int attack, int defense, int speed) {
    }

    private record PickedMove(String name, int power, Type type) {
    }
}
