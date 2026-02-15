package com.app.pokemon.client;

import com.app.pokemon.api.external.*;
import com.app.pokemon.exception.ExternalApiException;
import com.app.pokemon.exception.MoveNotFoundException;
import com.app.pokemon.exception.PokemonNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class PokemonApiClient {

    private final RestClient pokeApiRestClient;

    public PokemonApiClient(RestClient pokeApiRestClient) {
        this.pokeApiRestClient = pokeApiRestClient;
    }

    public PokemonApiResponse fetchPokemon(int id) {
        return getOrThrowByPokemonId("/pokemon/{id}", PokemonApiResponse.class, id);
    }

    public PokemonSpeciesResponse fetchSpecies(int id) {
        return getOrThrowByPokemonId("/pokemon-species/{id}", PokemonSpeciesResponse.class, id);
    }

    public PokemonMovesApiResponse fetchPokemonMoves(int id) {
        return getOrThrowByPokemonId("/pokemon/{id}", PokemonMovesApiResponse.class, id);
    }

    public PokemonBattleApiResponse fetchPokemonBattle(int id) {
        return getOrThrowByPokemonId("/pokemon/{id}", PokemonBattleApiResponse.class, id);
    }

    public PokemonMoveApiResponse fetchMove(String moveName) {
        try {
            return pokeApiRestClient.get()
                    .uri("/move/{name}", moveName)
                    .retrieve()
                    .body(PokemonMoveApiResponse.class);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new MoveNotFoundException("Move not found: name=" + moveName);
            }
            throw new ExternalApiException("Failed to call PokeAPI: /move/{name} name=" + moveName, ex);
        }
    }

    private <T> T getOrThrowByPokemonId(String path, Class<T> type, int id) {
        try {
            return pokeApiRestClient.get()
                    .uri(path, id)
                    .retrieve()
                    .body(type);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new PokemonNotFoundException(id);
            }
            throw new ExternalApiException("Failed to call PokeAPI: " + path + " id=" + id, ex);
        }
    }
}
