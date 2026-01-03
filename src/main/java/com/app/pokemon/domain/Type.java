package com.app.pokemon.domain;

public enum Type {

    NORMAL("ノーマル"),
    FIRE("ほのお"),
    WATER("みず"),
    ELECTRIC("でんき"),
    GRASS("くさ"),
    ICE("こおり"),
    FIGHTING("かくとう"),
    POISON("どく"),
    GROUND("じめん"),
    FLYING("ひこう"),
    PSYCHIC("エスパー"),
    BUG("むし"),
    ROCK("いわ"),
    GHOST("ゴースト"),
    DRAGON("ドラゴン"),
    DARK("あく"),
    STEEL("はがね"),
    FAIRY("フェアリー");

    private final String japaneseName;

    Type(String japaneseName) {
        this.japaneseName = japaneseName;
    }

    public String japaneseName() {
        return japaneseName;
    }
}
