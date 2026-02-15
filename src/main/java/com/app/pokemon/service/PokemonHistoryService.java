package com.app.pokemon.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonHistoryService {

    private static final int MAX_SIZE = 30;
    private static final String CACHE_NAME = "pokedexHistory";

    private final Cache cache;

    public PokemonHistoryService(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(CACHE_NAME);
        if (this.cache == null) {
            throw new IllegalStateException("Cache not found: " + CACHE_NAME);
        }
    }

    public List<String> recent(String key) {
        List<String> value = cache.get(key, List.class);
        if (value == null) return List.of();
        return List.copyOf(value);
    }

    public void add(String key, String pokemonName) {
        List<String> current = new ArrayList<>(recent(key));
        current.add(0, pokemonName);
        if (current.size() > MAX_SIZE) {
            current = current.subList(0, MAX_SIZE);
        }
        cache.put(key, List.copyOf(current));
    }

    public void clear(String key) {
        cache.evict(key);
    }
}
