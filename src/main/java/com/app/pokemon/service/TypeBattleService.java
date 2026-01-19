package com.app.pokemon.service;

import com.app.pokemon.domain.AdvantageResult;
import com.app.pokemon.view.PokemonView;
import com.app.pokemon.view.TypeBattleView;
import org.springframework.stereotype.Service;

@Service
public class TypeBattleService {

    private final PokemonService pokemonService;
    private final TypeEffectivenessService typeEffectivenessService;

    public TypeBattleService(PokemonService pokemonService,
                             TypeEffectivenessService typeEffectivenessService) {
        this.pokemonService = pokemonService;
        this.typeEffectivenessService = typeEffectivenessService;
    }

    public TypeBattleView compareById(int idA, int idB) {
        PokemonView a = pokemonService.getPokemonById(idA);
        PokemonView b = pokemonService.getPokemonById(idB);

        AdvantageResult result = typeEffectivenessService.judge(a.types(), b.types());

        String advantageLabel = switch (result.advantage()) {
            case A -> a.name() + " が有利";
            case B -> b.name() + " が有利";
            default -> "互角";
        };

        String aToBLabel =
                a.name() + " → " + b.name() +
                        " 最大倍率: " + result.aToB().multiplier() +
                        "（" + result.aToB().attackType().japaneseName() + "）";

        String bToALabel =
                b.name() + " → " + a.name() +
                        " 最大倍率: " + result.bToA().multiplier() +
                        "（" + result.bToA().attackType().japaneseName() + "）";

        return new TypeBattleView(
                a.id(), a.name(), a.types(), a.imageUrl(),
                b.id(), b.name(), b.types(), b.imageUrl(),
                result,
                advantageLabel,
                aToBLabel,
                bToALabel
        );

    }

}

