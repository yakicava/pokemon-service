package com.app.pokedex;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PokemonService {

    private final RestClient client = RestClient.create("https://pokeapi.co/api/v2");

    public PokemonView getPokemonById(int id) {
        PokemonApiResponse res = client.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .body(PokemonApiResponse.class);

        if (res == null) {
            throw new RuntimeException("Pokemon not found with id: " + id);
        }

        return new PokemonView(
                id,
                res.name(),
                "", // typeは今のところ空
                res.sprites() != null ? res.sprites().frontDefault() : null
        );
    }
}
