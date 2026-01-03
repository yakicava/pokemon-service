package com.app.pokemon.service;

import com.app.pokemon.api.PokemonApiResponse;
import com.app.pokemon.api.PokemonSpeciesResponse;
import com.app.pokemon.domain.Type;
import com.app.pokemon.mapper.PokemonTypeMapper;
import com.app.pokemon.view.PokemonView;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PokemonService {

    private final RestClient client =
            RestClient.create("https://pokeapi.co/api/v2");

    private final PokemonTypeMapper pokemonTypeMapper;

    public PokemonService(PokemonTypeMapper pokemonTypeMapper) {
        this.pokemonTypeMapper = pokemonTypeMapper;
    }

    public PokemonView getPokemonById(int id) {
        PokemonApiResponse res = fetchPokemonApiResponse(id);
        List<Type> types = pokemonTypeMapper.toTypes(res);

        String jaName = getJapaneseName(id);

        return new PokemonView(
                id,
                jaName, // 表示名は日本語
                types,
                res.sprites() != null ? res.sprites().frontDefault() : null
        );
    }

    private PokemonApiResponse fetchPokemonApiResponse(int id) {
        PokemonApiResponse res = client.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .body(PokemonApiResponse.class);

        if (res == null) {
            throw new RuntimeException("Pokemon not found with id: " + id);
        }
        return res;
    }

    private PokemonSpeciesResponse fetchSpecies(int id) {
        PokemonSpeciesResponse res = client.get()
                .uri("/pokemon-species/{id}", id)
                .retrieve()
                .body(PokemonSpeciesResponse.class);

        if (res == null) {
            throw new RuntimeException("Pokemon species not found with id: " + id);
        }
        return res;
    }

    private String getJapaneseName(int id) {
        PokemonSpeciesResponse species = fetchSpecies(id);
        if (species.names() == null) return "不明";

        return species.names().stream()
                .filter(n -> n.language() != null && "ja".equals(n.language().name()))
                .map(PokemonSpeciesResponse.NameEntry::name)
                .findFirst()
                .orElse("不明");
    }
}
