package com.app.pokemon.view;

import com.app.pokemon.domain.AdvantageResult;
import com.app.pokemon.domain.Type;

import java.util.List;

public record TypeBattleView(
        int aId, String aName, List<Type> aTypes, String aImageUrl,
        int bId, String bName, List<Type> bTypes, String bImageUrl,
        AdvantageResult result,
        String advantageLabel,
        String aToBLabel,
        String bToALabel
) {}
