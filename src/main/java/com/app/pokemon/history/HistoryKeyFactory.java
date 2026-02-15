package com.app.pokemon.history;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class HistoryKeyFactory {

    public String key(HttpSession session) {
        return "pokedex:history:" + session.getId();
    }
}
