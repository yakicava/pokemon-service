package com.app.pokemon.api.external;

public record PokemonMoveApiResponse(
        Integer power,
        NamedApiResource type
) {
    public record NamedApiResource(String name) {}
}
