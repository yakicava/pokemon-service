package com.app.pokemon.history;

import java.time.LocalDateTime;
import java.util.List;

public record SearchHistoryItem(
        int pokemonId,
        String name,
        List<String> types,
        String imageUrl,
        LocalDateTime searchedAt
) {}
