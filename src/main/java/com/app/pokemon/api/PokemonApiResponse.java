package com.app.pokemon.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PokemonApiResponse(
        String name,
        Sprites sprites,
        List<TypeSlot> types   // ★追加
) {

    public record Sprites(
            @JsonProperty("front_default") String frontDefault
    ) {}

    public record TypeSlot(
            int slot,
            TypeInfo type
    ) {}

    public record TypeInfo(
            String name
    ) {}
}
