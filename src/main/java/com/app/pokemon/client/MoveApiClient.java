package com.app.pokemon.client;

import com.app.pokemon.api.external.MoveApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MoveApiClient {

    private static final String MOVE_API =
            "https://pokeapi.co/api/v2/move/{name}";

    private final RestTemplate restTemplate = new RestTemplate();

    public MoveApiResponse fetchMove(String moveName) {
        return restTemplate.getForObject(
                MOVE_API,
                MoveApiResponse.class,
                moveName
        );
    }
}
