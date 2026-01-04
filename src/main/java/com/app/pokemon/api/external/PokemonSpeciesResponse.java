package com.app.pokemon.api.external;

import java.util.List;

public record PokemonSpeciesResponse(
        List<NameEntry> names
) {
    public record NameEntry(String name, Language language) {}
    public record Language(String name) {}
}
