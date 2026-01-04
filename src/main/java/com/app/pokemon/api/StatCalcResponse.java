package com.app.pokemon.api;

public record StatCalcResponse(
        int base,
        int iv,
        int ev,
        int level,
        StatKind kind,
        int result
) {}
