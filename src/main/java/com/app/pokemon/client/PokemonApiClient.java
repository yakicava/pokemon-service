package com.app.pokemon.client;

import com.app.pokemon.api.PokemonApiResponse;
import com.app.pokemon.api.PokemonSpeciesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PokemonApiClient {

    private final RestClient pokeApiRestClient;

    public PokemonApiClient(RestClient pokeApiRestClient) {
        this.pokeApiRestClient = pokeApiRestClient;
    }

    public PokemonApiResponse fetchPokemon(int id) {
        PokemonApiResponse res = pokeApiRestClient.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .body(PokemonApiResponse.class);

        if (res == null) {
            throw new RuntimeException("Pokemon not found: id=" + id);
        }
        return res;
    }

    public PokemonSpeciesResponse fetchSpecies(int id) {
        PokemonSpeciesResponse res = pokeApiRestClient.get()
                .uri("/pokemon-species/{id}", id)
                .retrieve()
                .body(PokemonSpeciesResponse.class);

        if (res == null) {
            throw new RuntimeException("Pokemon species not found: id=" + id);
        }
        return res;
    }
}
