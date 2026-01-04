package com.app.pokemon.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SvDexService {

    private static final Set<Integer> SV_IDS = Set.of(1,2,3,4,5,6);

    public boolean isInSv(int id) {
        return SV_IDS.contains(id);
    }
}

