package com.app.pokemon.service;

import com.app.pokemon.api.external.PokemonMovesApiResponse;
import com.app.pokemon.client.PokemonApiClient;
import com.app.pokemon.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class MoveSelectService {

    private static final String VERSION_GROUP_SV = "scarlet-violet";
    private static final String LEARN_METHOD_LEVEL_UP = "level-up";

    private final PokemonApiClient pokemonApiClient;

    public MoveSelectService(PokemonApiClient pokemonApiClient) {
        this.pokemonApiClient = pokemonApiClient;
    }

    /**
     * SV準拠：Lv50までに「レベルアップ」で覚える技名（pokeapi move name）
     */
    public List<String> selectLevelUpMovesUpTo50Sv(int pokemonId) {
        if (pokemonId <= 0) {
            throw new BadRequestException("pokemonId must be > 0");
        }

        PokemonMovesApiResponse res = pokemonApiClient.fetchPokemonMoves(pokemonId);
        if (res == null || res.moves() == null) {
            return List.of();
        }

        Set<String> names = new LinkedHashSet<>();

        for (PokemonMovesApiResponse.MoveEntry entry : res.moves()) {
            if (entry == null || entry.move() == null || entry.move().name() == null) continue;
            if (entry.version_group_details() == null) continue;

            boolean learnedInSvUpTo50 = entry.version_group_details().stream().anyMatch(d ->
                    d != null
                            && d.level_learned_at() <= 50
                            && d.move_learn_method() != null
                            && LEARN_METHOD_LEVEL_UP.equals(d.move_learn_method().name())
                            && d.version_group() != null
                            && VERSION_GROUP_SV.equals(d.version_group().name())
            );

            if (learnedInSvUpTo50) {
                names.add(entry.move().name());
            }
        }

        return names.stream().toList();
    }
}
