package com.app.pokemon.service;

import com.app.pokemon.api.DamageCalcRequest;
import com.app.pokemon.api.DamageCalcResponse;
import com.app.pokemon.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class DamageCalcService {

    /**
     * ダメージ計算（簡略版）
     *
     * base = floor( ( (2*L/5 + 2) * Power * A / D ) / 50 ) + 2
     * modifier = STAB * effectiveness
     * damage = floor(base * modifier)
     */
    public DamageCalcResponse calc(DamageCalcRequest req) {
        validate(req);

        int base = calcBase(req.level(), req.power(), req.attack(), req.defense());
        double modifier = (req.stab() ? 1.5 : 1.0) * req.effectiveness();
        int damage = (int) Math.floor(base * modifier);

        return new DamageCalcResponse(damage);
    }

    private int calcBase(int level, int power, int attack, int defense) {
        long step1 = (2L * level) / 5L + 2;
        long step2 = step1 * power * attack;
        long step3 = step2 / defense;
        long step4 = step3 / 50;
        return (int) (step4 + 2);
    }

    private void validate(DamageCalcRequest req) {
        if (req.level() < 1 || req.level() > 100)
            throw new BadRequestException("level must be 1..100");
        if (req.power() < 1 || req.power() > 300)
            throw new BadRequestException("power must be 1..300");
        if (req.attack() < 1)
            throw new BadRequestException("attack must be >= 1");
        if (req.defense() < 1)
            throw new BadRequestException("defense must be >= 1");
        if (req.effectiveness() <= 0)
            throw new BadRequestException("effectiveness must be > 0");
    }
}
