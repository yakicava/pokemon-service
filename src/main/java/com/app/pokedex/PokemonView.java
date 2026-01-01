package com.app.pokedex;

public record PokemonView(
        int id,
        String name,
        String type,
        String imageUrl
) {}
