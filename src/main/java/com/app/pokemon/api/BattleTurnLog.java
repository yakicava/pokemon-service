package com.app.pokemon.api;

public record BattleTurnLog(
        int turn,
        String attacker,   // "A" or "B"
        int damage,
        int hpAAfter,
        int hpBAfter
) {}
