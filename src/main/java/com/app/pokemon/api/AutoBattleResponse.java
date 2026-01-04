package com.app.pokemon.api;

public record AutoBattleResponse(
        int pokemonAId,
        int pokemonBId,
        SelectedMove selectedMoveA,
        SelectedMove selectedMoveB,
        FullBattleResponse battle
) {}
