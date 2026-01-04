package com.app.pokemon.service;

import com.app.pokemon.api.DamageCalcRequest;
import com.app.pokemon.api.SelectedMove;
import com.app.pokemon.api.external.MoveApiResponse;
import com.app.pokemon.client.MoveApiClient;
import com.app.pokemon.domain.Type;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AutoMovePickerService {

    private final MoveSelectService moveSelectService;
    private final MoveApiClient moveApiClient;
    private final DamageCalcService damageCalcService;
    private final TypeEffectivenessService typeEffectivenessService;

    public AutoMovePickerService(
            MoveSelectService moveSelectService,
            MoveApiClient moveApiClient,
            DamageCalcService damageCalcService,
            TypeEffectivenessService typeEffectivenessService
    ) {
        this.moveSelectService = moveSelectService;
        this.moveApiClient = moveApiClient;
        this.damageCalcService = damageCalcService;
        this.typeEffectivenessService = typeEffectivenessService;
    }

    public SelectedMove pickBestMove(
            int attackerPokemonId,
            List<Type> attackerTypes,
            List<Type> defenderTypes,
            int level,
            int attack,      // 物理
            int defense,     // 物理
            int spAttack,    // 特殊
            int spDefense,   // 特殊
            int topN
    ) {
        if (attackerPokemonId <= 0) throw new IllegalArgumentException("attackerPokemonId must be > 0");
        if (attackerTypes == null || attackerTypes.isEmpty()) throw new IllegalArgumentException("attackerTypes required");
        if (defenderTypes == null || defenderTypes.isEmpty()) throw new IllegalArgumentException("defenderTypes required");
        if (level <= 0) throw new IllegalArgumentException("level must be > 0");

        List<String> candidates = moveSelectService.selectLevelUpMovesUpTo50Sv(attackerPokemonId);
        if (candidates.isEmpty()) {
            throw new IllegalStateException("No move candidates found for pokemonId=" + attackerPokemonId);
        }

        int limit = (topN <= 0) ? 10 : topN;

        // move詳細を引いて、damaging moves だけ集める
        List<MoveMeta> metas = new ArrayList<>();
        for (String name : candidates) {
            fetchMoveMeta(name).ifPresent(metas::add);
        }

        // 威力順で上位Nだけ評価（重いので）
        List<MoveMeta> top = metas.stream()
                .sorted(Comparator.comparingInt((MoveMeta m) -> m.power).reversed())
                .limit(limit)
                .toList();

        if (top.isEmpty()) {
            throw new IllegalStateException("No damaging moves found (power>0, not status) for pokemonId=" + attackerPokemonId);
        }

        Type def1 = defenderTypes.get(0);
        Type def2 = defenderTypes.size() >= 2 ? defenderTypes.get(1) : null;

        SelectedMove best = null;
        int bestDamage = -1;

        for (MoveMeta m : top) {
            boolean stab = attackerTypes.contains(m.type);
            double eff = typeEffectivenessService.multiplier(m.type, def1, def2);

            int atkUsed = m.damageClass.equals("physical") ? attack : spAttack;
            int defUsed = m.damageClass.equals("physical") ? defense : spDefense;

            if (atkUsed <= 0 || defUsed <= 0) continue;

            int dmg = damageCalcService.calc(
                    new DamageCalcRequest(level, m.power, atkUsed, defUsed, stab, eff)
            ).damage();

            if (dmg > bestDamage) {
                bestDamage = dmg;
                best = new SelectedMove(m.name, m.power, m.type, m.damageClass, stab, dmg);
            }
        }

        return best;
    }

    private Optional<MoveMeta> fetchMoveMeta(String moveName) {
        try {
            MoveApiResponse res = moveApiClient.fetchMove(moveName);
            if (res == null) return Optional.empty();
            if (res.type() == null || res.type().name() == null) return Optional.empty();
            if (res.damage_class() == null || res.damage_class().name() == null) return Optional.empty();

            String dc = res.damage_class().name(); // "physical" / "special" / "status"
            if (!dc.equals("physical") && !dc.equals("special")) return Optional.empty(); // status除外

            Integer power = res.power();
            if (power == null || power <= 0) return Optional.empty();

            Type type = Type.valueOf(res.type().name().toUpperCase(Locale.ROOT));
            return Optional.of(new MoveMeta(moveName, power, type, dc));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private record MoveMeta(String name, int power, Type type, String damageClass) {}
}
