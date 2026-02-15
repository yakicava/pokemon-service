package com.app.pokemon.controller;

import com.app.pokemon.history.HistoryKeyFactory;
import com.app.pokemon.service.PokemonHistoryService;
import com.app.pokemon.service.PokemonService;
import com.app.pokemon.view.PokemonView;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pokedex")
public class PokedexController {

    private static final Logger log = LoggerFactory.getLogger(PokedexController.class);

    private final PokemonService pokemonService;
    private final PokemonHistoryService historyService;
    private final HistoryKeyFactory keyFactory;

    public PokedexController(
            PokemonService pokemonService,
            PokemonHistoryService historyService,
            HistoryKeyFactory keyFactory
    ) {
        this.pokemonService = pokemonService;
        this.historyService = historyService;
        this.keyFactory = keyFactory;
    }

    @GetMapping
    public String showForm() {
        log.info("PokedexController#showForm");
        return "pokedex";
    }

    @PostMapping("/result")
    public String showResult(@RequestParam int id, Model model, HttpSession session) {
        log.info("PokedexController#showResult id={}", id);

        PokemonView pokemon = pokemonService.getPokemonById(id);
        model.addAttribute("pokemon", pokemon);

        historyService.add(keyFactory.key(session), pokemon.name());

        return "pokedex-result";
    }
}
