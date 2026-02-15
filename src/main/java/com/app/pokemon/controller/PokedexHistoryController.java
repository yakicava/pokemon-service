package com.app.pokemon.controller;

import com.app.pokemon.history.HistoryKeyFactory;
import com.app.pokemon.service.PokemonHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pokedex/history")
public class PokedexHistoryController {

    private final PokemonHistoryService historyService;
    private final HistoryKeyFactory keyFactory;

    public PokedexHistoryController(PokemonHistoryService historyService, HistoryKeyFactory keyFactory) {
        this.historyService = historyService;
        this.keyFactory = keyFactory;
    }

    @GetMapping
    public String history(Model model, HttpSession session) {
        String key = keyFactory.key(session);
        model.addAttribute("histories", historyService.recent(key));
        return "history";
    }

    @PostMapping("/clear")
    public String clear(HttpSession session, RedirectAttributes ra) {
        String key = keyFactory.key(session);
        historyService.clear(key);
        ra.addFlashAttribute("message", "履歴を削除しました");
        return "redirect:/pokedex/history";
    }
}
