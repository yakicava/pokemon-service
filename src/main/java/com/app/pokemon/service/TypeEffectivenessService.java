package com.app.pokemon.service;

import com.app.pokemon.domain.Advantage;
import com.app.pokemon.domain.AdvantageResult;
import com.app.pokemon.domain.BestHit;
import com.app.pokemon.domain.Type;
import com.app.pokemon.domain.TypeChart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeEffectivenessService {

    private double effectiveness(Type atk, List<Type> defs) {
        double result = 1.0;
        for (Type def : defs) {
            result *= TypeChart.effectiveness(atk, def);
        }
        return result;
    }

    public BestHit bestAgainst(List<Type> attackerTypes,
                               List<Type> defenderTypes) {

        BestHit best = new BestHit(null, 0.0);

        for (Type atk : attackerTypes) {
            double m = effectiveness(atk, defenderTypes);
            if (m > best.multiplier()) {
                best = new BestHit(atk, m);
            }
        }
        return best;
    }

    public AdvantageResult judge(List<Type> typesA,
                                 List<Type> typesB) {

        BestHit aToB = bestAgainst(typesA, typesB);
        BestHit bToA = bestAgainst(typesB, typesA);

        Advantage advantage;
        if (aToB.multiplier() > bToA.multiplier()) {
            advantage = Advantage.A;
        } else if (aToB.multiplier() < bToA.multiplier()) {
            advantage = Advantage.B;
        } else {
            advantage = Advantage.EVEN;
        }

        return new AdvantageResult(aToB, bToA, advantage);
    }

    public double multiplier(Type moveType, List<Type> defenderTypes) {
        return effectiveness(moveType, defenderTypes);
    }

    public double multiplier(Type moveType, Type def1, Type def2) {
        if (def2 == null) {
            return TypeChart.effectiveness(moveType, def1);
        }
        return TypeChart.effectiveness(moveType, def1) * TypeChart.effectiveness(moveType, def2);
    }

}
