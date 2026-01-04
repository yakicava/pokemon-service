package com.app.pokemon.service;

import com.app.pokemon.exception.BadRequestException;
import com.app.pokemon.api.StatCalcRequest;
import com.app.pokemon.api.StatCalcResponse;
import org.springframework.stereotype.Service;

@Service
public class StatCalcService {

    public StatCalcResponse calc(StatCalcRequest req) {
        validate(req);

        int result = switch (req.kind()) {
            case HP -> calcHp(req.base(), req.iv(), req.ev(), req.level());
            case OTHER -> calcStat(req.base(), req.iv(), req.ev(), req.level());
        };

        return new StatCalcResponse(
                req.base(),
                req.iv(),
                req.ev(),
                req.level(),
                req.kind(),
                result
        );
    }

    private void validate(StatCalcRequest req) {
        if (req.level() < 1 || req.level() > 100) {
            throw new BadRequestException("level must be 1..100");
        }
        if (req.base() < 1 || req.base() > 255) {
            throw new BadRequestException("base must be 1..255");
        }
        if (req.iv() < 0 || req.iv() > 31) {
            throw new BadRequestException("iv must be 0..31");
        }
        if (req.ev() < 0 || req.ev() > 252) {
            throw new BadRequestException("ev must be 0..252");
        }
        if (req.ev() % 4 != 0) {
            throw new BadRequestException("ev must be multiple of 4 (0,4,8,...,252)");
        }
    }

    public int calcHp(int base, int iv, int ev, int level) {
        return ((base * 2 + iv + ev / 4) * level) / 100 + level + 10;
    }

    public int calcStat(int base, int iv, int ev, int level) {
        return ((base * 2 + iv + ev / 4) * level) / 100 + 5;
    }
}
