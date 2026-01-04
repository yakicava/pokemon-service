package com.app.pokemon.api;

import java.util.List;

public record FullBattleResponse(
        String first,          // "A" or "B"
        int turns,
        int hpAAfter,
        int hpBAfter,
        String winner,         // "A" / "B" / "DRAW"
        List<BattleTurnLog> logs
) {}
