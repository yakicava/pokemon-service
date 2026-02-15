package com.app.pokemon.exception;

public class PokemonNotFoundException extends RuntimeException {

    private final Integer id;

    public PokemonNotFoundException(int id) {
        super("Pokemon not found: id=" + id);
        this.id = id;
    }

    public PokemonNotFoundException(int id, String message) {
        super(message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
