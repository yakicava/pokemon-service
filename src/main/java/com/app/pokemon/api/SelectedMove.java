package com.app.pokemon.api;

import com.app.pokemon.domain.Type;

public record SelectedMove(
        String name,
        int power,
        Type type,
        String damageClass,   // "physical" / "special"
        boolean stab,
        int expectedDamage
) {}
