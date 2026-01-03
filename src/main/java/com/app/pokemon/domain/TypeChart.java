package com.app.pokemon.domain;

import java.util.EnumMap;
import java.util.Map;

public final class TypeChart {

    private static final Map<Type, Map<Type, Double>> CHART =
            new EnumMap<>(Type.class);

    static {
        // ===== NORMAL =====
        put(Type.NORMAL, Type.ROCK, 0.5);
        put(Type.NORMAL, Type.STEEL, 0.5);
        put(Type.NORMAL, Type.GHOST, 0.0);

        // ===== FIRE =====
        put(Type.FIRE, Type.GRASS, 2.0);
        put(Type.FIRE, Type.ICE, 2.0);
        put(Type.FIRE, Type.BUG, 2.0);
        put(Type.FIRE, Type.STEEL, 2.0);
        put(Type.FIRE, Type.FIRE, 0.5);
        put(Type.FIRE, Type.WATER, 0.5);
        put(Type.FIRE, Type.ROCK, 0.5);
        put(Type.FIRE, Type.DRAGON, 0.5);

        // ===== WATER =====
        put(Type.WATER, Type.FIRE, 2.0);
        put(Type.WATER, Type.GROUND, 2.0);
        put(Type.WATER, Type.ROCK, 2.0);
        put(Type.WATER, Type.WATER, 0.5);
        put(Type.WATER, Type.GRASS, 0.5);
        put(Type.WATER, Type.DRAGON, 0.5);

        // ===== ELECTRIC =====
        put(Type.ELECTRIC, Type.WATER, 2.0);
        put(Type.ELECTRIC, Type.FLYING, 2.0);
        put(Type.ELECTRIC, Type.ELECTRIC, 0.5);
        put(Type.ELECTRIC, Type.GRASS, 0.5);
        put(Type.ELECTRIC, Type.DRAGON, 0.5);
        put(Type.ELECTRIC, Type.GROUND, 0.0);

        // ===== GRASS =====
        put(Type.GRASS, Type.WATER, 2.0);
        put(Type.GRASS, Type.GROUND, 2.0);
        put(Type.GRASS, Type.ROCK, 2.0);
        put(Type.GRASS, Type.FIRE, 0.5);
        put(Type.GRASS, Type.GRASS, 0.5);
        put(Type.GRASS, Type.POISON, 0.5);
        put(Type.GRASS, Type.FLYING, 0.5);
        put(Type.GRASS, Type.BUG, 0.5);
        put(Type.GRASS, Type.DRAGON, 0.5);
        put(Type.GRASS, Type.STEEL, 0.5);

        // ===== ICE =====
        put(Type.ICE, Type.GRASS, 2.0);
        put(Type.ICE, Type.GROUND, 2.0);
        put(Type.ICE, Type.FLYING, 2.0);
        put(Type.ICE, Type.DRAGON, 2.0);
        put(Type.ICE, Type.FIRE, 0.5);
        put(Type.ICE, Type.WATER, 0.5);
        put(Type.ICE, Type.ICE, 0.5);
        put(Type.ICE, Type.STEEL, 0.5);

        // ===== FIGHTING =====
        put(Type.FIGHTING, Type.NORMAL, 2.0);
        put(Type.FIGHTING, Type.ICE, 2.0);
        put(Type.FIGHTING, Type.ROCK, 2.0);
        put(Type.FIGHTING, Type.DARK, 2.0);
        put(Type.FIGHTING, Type.STEEL, 2.0);
        put(Type.FIGHTING, Type.POISON, 0.5);
        put(Type.FIGHTING, Type.FLYING, 0.5);
        put(Type.FIGHTING, Type.PSYCHIC, 0.5);
        put(Type.FIGHTING, Type.BUG, 0.5);
        put(Type.FIGHTING, Type.FAIRY, 0.5);
        put(Type.FIGHTING, Type.GHOST, 0.0);

        // ===== POISON =====
        put(Type.POISON, Type.GRASS, 2.0);
        put(Type.POISON, Type.FAIRY, 2.0);
        put(Type.POISON, Type.POISON, 0.5);
        put(Type.POISON, Type.GROUND, 0.5);
        put(Type.POISON, Type.ROCK, 0.5);
        put(Type.POISON, Type.GHOST, 0.5);
        put(Type.POISON, Type.STEEL, 0.0);

        // ===== GROUND =====
        put(Type.GROUND, Type.FIRE, 2.0);
        put(Type.GROUND, Type.ELECTRIC, 2.0);
        put(Type.GROUND, Type.POISON, 2.0);
        put(Type.GROUND, Type.ROCK, 2.0);
        put(Type.GROUND, Type.STEEL, 2.0);
        put(Type.GROUND, Type.GRASS, 0.5);
        put(Type.GROUND, Type.BUG, 0.5);
        put(Type.GROUND, Type.FLYING, 0.0);

        // ===== FLYING =====
        put(Type.FLYING, Type.GRASS, 2.0);
        put(Type.FLYING, Type.FIGHTING, 2.0);
        put(Type.FLYING, Type.BUG, 2.0);
        put(Type.FLYING, Type.ELECTRIC, 0.5);
        put(Type.FLYING, Type.ROCK, 0.5);
        put(Type.FLYING, Type.STEEL, 0.5);

        // ===== PSYCHIC =====
        put(Type.PSYCHIC, Type.FIGHTING, 2.0);
        put(Type.PSYCHIC, Type.POISON, 2.0);
        put(Type.PSYCHIC, Type.PSYCHIC, 0.5);
        put(Type.PSYCHIC, Type.STEEL, 0.5);
        put(Type.PSYCHIC, Type.DARK, 0.0);

        // ===== BUG =====
        put(Type.BUG, Type.GRASS, 2.0);
        put(Type.BUG, Type.PSYCHIC, 2.0);
        put(Type.BUG, Type.DARK, 2.0);
        put(Type.BUG, Type.FIRE, 0.5);
        put(Type.BUG, Type.FIGHTING, 0.5);
        put(Type.BUG, Type.POISON, 0.5);
        put(Type.BUG, Type.FLYING, 0.5);
        put(Type.BUG, Type.GHOST, 0.5);
        put(Type.BUG, Type.STEEL, 0.5);
        put(Type.BUG, Type.FAIRY, 0.5);

        // ===== ROCK =====
        put(Type.ROCK, Type.FIRE, 2.0);
        put(Type.ROCK, Type.ICE, 2.0);
        put(Type.ROCK, Type.FLYING, 2.0);
        put(Type.ROCK, Type.BUG, 2.0);
        put(Type.ROCK, Type.FIGHTING, 0.5);
        put(Type.ROCK, Type.GROUND, 0.5);
        put(Type.ROCK, Type.STEEL, 0.5);

        // ===== GHOST =====
        put(Type.GHOST, Type.PSYCHIC, 2.0);
        put(Type.GHOST, Type.GHOST, 2.0);
        put(Type.GHOST, Type.DARK, 0.5);
        put(Type.GHOST, Type.NORMAL, 0.0);

        // ===== DRAGON =====
        put(Type.DRAGON, Type.DRAGON, 2.0);
        put(Type.DRAGON, Type.STEEL, 0.5);
        put(Type.DRAGON, Type.FAIRY, 0.0);

        // ===== DARK =====
        put(Type.DARK, Type.PSYCHIC, 2.0);
        put(Type.DARK, Type.GHOST, 2.0);
        put(Type.DARK, Type.FIGHTING, 0.5);
        put(Type.DARK, Type.DARK, 0.5);
        put(Type.DARK, Type.FAIRY, 0.5);

        // ===== STEEL =====
        put(Type.STEEL, Type.ICE, 2.0);
        put(Type.STEEL, Type.ROCK, 2.0);
        put(Type.STEEL, Type.FAIRY, 2.0);
        put(Type.STEEL, Type.FIRE, 0.5);
        put(Type.STEEL, Type.WATER, 0.5);
        put(Type.STEEL, Type.ELECTRIC, 0.5);
        put(Type.STEEL, Type.STEEL, 0.5);

        // ===== FAIRY =====
        put(Type.FAIRY, Type.FIGHTING, 2.0);
        put(Type.FAIRY, Type.DRAGON, 2.0);
        put(Type.FAIRY, Type.DARK, 2.0);
        put(Type.FAIRY, Type.FIRE, 0.5);
        put(Type.FAIRY, Type.POISON, 0.5);
        put(Type.FAIRY, Type.STEEL, 0.5);
    }

    private TypeChart() {}

    private static void put(Type atk, Type def, double value) {
        CHART.computeIfAbsent(atk, k -> new EnumMap<>(Type.class))
                .put(def, value);
    }

    /** 攻撃タイプatkが、防御タイプdefに与える倍率（未定義は1.0） */
    public static double effectiveness(Type atk, Type def) {
        return CHART
                .getOrDefault(atk, Map.of())
                .getOrDefault(def, 1.0);
    }
}
