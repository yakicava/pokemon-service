package com.app.pokemon.view;

import com.app.pokemon.domain.Type;
import com.app.pokemon.domain.AdvantageResult;

import java.util.List;

public record TypeBattleView(
        int idA,
        String nameA,
        List<Type> typesA,

        int idB,
        String nameB,
        List<Type> typesB,

        AdvantageResult result,

        String advantageLabel,
        String aToBLabel,
        String bToALabel)
{}



