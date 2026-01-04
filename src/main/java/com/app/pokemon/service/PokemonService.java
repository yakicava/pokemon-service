package com.app.pokemon.service;

import com.app.pokemon.api.external.PokemonSpeciesResponse;
import com.app.pokemon.client.PokemonApiClient;
import com.app.pokemon.mapper.PokemonTypeMapper;
import com.app.pokemon.view.PokemonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private static final Logger log =
            LoggerFactory.getLogger(PokemonService.class);

    private final PokemonApiClient pokemonApiClient;
    private final PokemonTypeMapper pokemonTypeMapper;

    public PokemonService(PokemonApiClient pokemonApiClient,
                          PokemonTypeMapper pokemonTypeMapper) {
        this.pokemonApiClient = pokemonApiClient;
        this.pokemonTypeMapper = pokemonTypeMapper;
    }

    public PokemonView getPokemonById(int id, String requestId) {
        String rid = requestId == null ? "N/A" : requestId;
        log.info("PokemonService#getPokemonById requestId={} id={}", rid, id);

        var res = pokemonApiClient.fetchPokemon(id);
        var species = pokemonApiClient.fetchSpecies(id);

        var types = pokemonTypeMapper.toTypes(res);
        var jaName = extractJapaneseName(species);

        return new PokemonView(
                id,
                jaName,
                types,
                res.sprites() != null ? res.sprites().frontDefault() : null
        );
    }

    private String extractJapaneseName(PokemonSpeciesResponse species) {
        if (species.names() == null) return "不明";

        return species.names().stream()
                .filter(n -> n.language() != null && "ja".equals(n.language().name()))
                .map(PokemonSpeciesResponse.NameEntry::name)
                .findFirst()
                .orElse("不明");
    }
}
