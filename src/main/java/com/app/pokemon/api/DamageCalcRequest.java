package com.app.pokemon.api;

public record DamageCalcRequest(
        int level,
        int power,
        int attack,
        int defense,
        boolean stab,
        double effectiveness   // 0.5 / 1.0 / 2.0 / 4.0
) {}
