package com.app.pokemon.api.external;

public record MoveApiResponse(
        Integer power,
        TypeInfo type,
        DamageClass damage_class
) {
    public record TypeInfo(String name) {}
    public record DamageClass(String name) {} // "physical" / "special" / "status"
}
