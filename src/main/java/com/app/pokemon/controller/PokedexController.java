package com.app.pokemon.controller;

import com.app.pokemon.service.PokemonService;
import com.app.pokemon.view.PokemonView;
import jakarta.servlet.http.HttpServletRequest;
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

    public PokedexController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public String showForm(HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        log.info("PokedexController#showForm requestId={}", requestId);

        return "pokedex";
    }

    @PostMapping("/result")
    public String showResult(@RequestParam int id, Model model, HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        log.info("PokedexController#showResult requestId={} id={}", requestId, id);

        PokemonView pokemon = pokemonService.getPokemonById(id, requestId);
        model.addAttribute("pokemon", pokemon);

        return "pokedex-result";
    }
}
