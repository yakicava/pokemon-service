package com.app.pokemon.api;

/** SV準拠バトルの入力（図鑑番号だけ） */
public record SvBattleRequest(
        int attackerId,
        int defenderId
) {}
