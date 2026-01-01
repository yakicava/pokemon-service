package com.app.pokedex;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PokemonApiResponse(
        String name,
        Sprites sprites
) {
    public record Sprites(
            @JsonProperty("front_default") String frontDefault
    ) {}
}
