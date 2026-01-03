package com.app.pokemon.domain;

public record AdvantageResult(
        BestHit aToB,
        BestHit bToA,
        Advantage advantage
) {}
