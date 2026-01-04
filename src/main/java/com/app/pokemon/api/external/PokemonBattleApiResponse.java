package com.app.pokemon.api.external;

import java.util.List;

public record PokemonBattleApiResponse(
        List<TypeSlot> types,
        List<StatSlot> stats
) {
    public record TypeSlot(int slot, TypeInfo type) {}
    public record TypeInfo(String name) {}

    public record StatSlot(int base_stat, StatInfo stat) {}
    public record StatInfo(String name) {}
}
