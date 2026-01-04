package com.app.pokemon.config;

import com.app.pokemon.config.properties.PokeApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(PokeApiProperties.class)
public class PokeApiConfig {

    @Bean
    public RestClient pokeApiRestClient(
            RestClient.Builder builder,
            PokeApiProperties props) {

        return builder
                .baseUrl(props.getBaseUrl())
                .build();
    }
}


