package com.app.pokemon.view;

import com.app.pokemon.domain.Type;
import java.util.List;

public record PokemonView(
        int id,
        String name,
        List<Type> types,
        String imageUrl
) {}

