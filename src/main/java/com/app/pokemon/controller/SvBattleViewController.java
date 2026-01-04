package com.app.pokemon.controller;

import com.app.pokemon.api.FullBattleResponse;
import com.app.pokemon.exception.SvNotAvailableException;
import com.app.pokemon.service.PokemonService;
import com.app.pokemon.service.SvBattleService;
import com.app.pokemon.view.PokemonView;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sv-battle")
public class SvBattleViewController {

    private final SvBattleService svBattleService;
    private final PokemonService pokemonService;

    public SvBattleViewController(SvBattleService svBattleService, PokemonService pokemonService) {
        this.svBattleService = svBattleService;
        this.pokemonService = pokemonService;
    }

    // 初回は空欄で出したいので、modelに何も入れない
    @GetMapping
    public String showForm() {
        return "sv-battle-form";
    }

    @PostMapping("/result")
    public String showResult(@RequestParam int attackerId,
                             @RequestParam int defenderId,
                             Model model,
                             HttpServletRequest request) {

        String requestId = (String) request.getAttribute("requestId");

        try {
            FullBattleResponse res =
                    svBattleService.fullBattleByIds(attackerId, defenderId);

            PokemonView attacker =
                    pokemonService.getPokemonById(attackerId, requestId);
            PokemonView defender =
                    pokemonService.getPokemonById(defenderId, requestId);

            model.addAttribute("attackerId", attackerId);
            model.addAttribute("attackerImage", attacker.imageUrl());
            model.addAttribute("defenderId", defenderId);
            model.addAttribute("defenderImage", defender.imageUrl());
            model.addAttribute("attackerName", attacker.name());
            model.addAttribute("defenderName", defender.name());
            model.addAttribute("result", res);

            return "sv-battle-result";

        } catch (SvNotAvailableException e) {
            // ★ SVにいない場合はここに来る

            model.addAttribute("attackerId", attackerId);
            model.addAttribute("defenderId", defenderId);
            model.addAttribute("error", e.getMessage());

            // 入力画面に戻す
            return "sv-battle-form";
        }
    }

}
