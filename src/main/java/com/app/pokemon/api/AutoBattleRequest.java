package com.app.pokemon.api;

/**
 * 画面からの最小入力
 * 技・実数値・タイプはサーバ側で自動解決する
 */
public record AutoBattleRequest(
        int pokemonAId,
        int pokemonBId
) {}
