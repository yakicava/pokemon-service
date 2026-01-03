package com.app.pokemon.controller;

import com.app.pokemon.service.PokemonService;
import com.app.pokemon.view.PokemonView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pokedex")
public class PokedexController {

    private final PokemonService pokemonService;

    public PokedexController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    // 図鑑入力画面
    @GetMapping
    public String showForm() {
        return "pokedex";
    }

    // 図鑑検索結果
    @PostMapping("/result")
    public String showResult(@RequestParam int id, Model model) {
        PokemonView pokemon = pokemonService.getPokemonById(id);
        model.addAttribute("pokemon", pokemon);
        return "pokedex-result";
    }
}
