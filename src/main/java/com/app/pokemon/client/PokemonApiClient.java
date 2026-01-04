package com.app.pokemon.client;

import com.app.pokemon.api.external.*;

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

    public PokemonMovesApiResponse fetchPokemonMoves(int id) {
        PokemonMovesApiResponse res = pokeApiRestClient.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .body(PokemonMovesApiResponse.class);

        if (res == null) {
            throw new RuntimeException("Pokemon moves not found: id=" + id);
        }
        return res;
    }

    public PokemonBattleApiResponse fetchPokemonBattle(int id) {
        PokemonBattleApiResponse res = pokeApiRestClient.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .body(PokemonBattleApiResponse.class);

        if (res == null) {
            throw new RuntimeException("Pokemon battle data not found: id=" + id);
        }
        return res;
    }

    public PokemonMoveApiResponse fetchMove(String moveName) {
        PokemonMoveApiResponse res = pokeApiRestClient.get()
                .uri("/move/{name}", moveName)
                .retrieve()
                .body(PokemonMoveApiResponse.class);

        if (res == null) {
            throw new RuntimeException("Move not found: name=" + moveName);
        }
        return res;
    }

}
