package com.app.pokemon.api.external;

import java.util.List;

public record PokemonMovesApiResponse(
        List<MoveEntry> moves
) {
    public record MoveEntry(
            MoveRef move,
            List<VersionGroupDetail> version_group_details
    ) {}

    public record MoveRef(String name) {}

    public record VersionGroupDetail(
            int level_learned_at,
            NamedRef move_learn_method,
            NamedRef version_group
    ) {}

    public record NamedRef(String name) {}
}
