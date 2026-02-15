package com.app.pokemon.exception;

public class MoveNotFoundException extends RuntimeException {
    public MoveNotFoundException(String message) {
        super(message);
    }
}
