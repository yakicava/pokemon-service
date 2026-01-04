package com.app.pokemon.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class SpeedOrderService {

    /**
     * 先攻判定
     */
    public String decideFirst(int speedA, int speedB) {
        if (speedA > speedB) {
            return "A";
        }
        if (speedA < speedB) {
            return "B";
        }
        // 同速：1/2ランダム
        return ThreadLocalRandom.current().nextBoolean() ? "A" : "B";
    }
}

