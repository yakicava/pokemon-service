package com.app.pokemon.api;

public record SvBattleViewResponse(
        int attackerId,
        int defenderId,
        String pickedMoveA,
        String pickedMoveB,
        int damageAtoB,
        int damageBtoA
) {}
