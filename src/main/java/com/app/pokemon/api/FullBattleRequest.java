package com.app.pokemon.api;

import com.app.pokemon.domain.Type;

/**
 * 勝敗決定まで回すための最小入力（実数値を直接渡す）
 * Lv50固定・乱数なし（同速のみ1/2）
 */
public record FullBattleRequest(
        // Aの実数値
        int hpA,
        int attackA,
        int defenseA,
        int speedA,
        int powerA,
        Type moveTypeA,
        boolean stabA,
        Type typeA1,
        Type typeA2,   // null可

        // Bの実数値
        int hpB,
        int attackB,
        int defenseB,
        int speedB,
        int powerB,
        Type moveTypeB,
        boolean stabB,
        Type typeB1,
        Type typeB2    // null可
) {}
