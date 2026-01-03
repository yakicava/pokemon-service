package com.app.pokemon.mapper;

import com.app.pokemon.api.PokemonApiResponse;
import com.app.pokemon.domain.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PokemonTypeMapper {

    public List<Type> toTypes(PokemonApiResponse apiResponse) {
        if (apiResponse.types() == null) return List.of();

        return apiResponse.types().stream()
                .map(slot -> slot.type().name())
                .filter(name -> name != null && !name.isBlank())
                .map(String::toUpperCase)
                .map(Type::valueOf)
                .toList();
    }
}
