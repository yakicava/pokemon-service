package com.app.pokemon.api;

public record StatCalcRequest(
        int base,
        int iv,
        int ev,
        int level,
        StatKind kind
) {}
