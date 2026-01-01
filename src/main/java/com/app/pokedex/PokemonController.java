package com.app.pokedex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/pokemon")
    public String search(@RequestParam int id, Model model) {
        PokemonView pokemon = pokemonService.getPokemonById(id);

        model.addAttribute("pokemon", pokemon);

        return "result";
    }

}
