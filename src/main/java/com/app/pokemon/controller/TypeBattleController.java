package com.app.pokemon.controller;

import com.app.pokemon.service.TypeBattleService;
import com.app.pokemon.view.TypeBattleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/type-battle")
public class TypeBattleController {

    private static final Logger log = LoggerFactory.getLogger(TypeBattleController.class);

    private final TypeBattleService typeBattleService;

    public TypeBattleController(TypeBattleService typeBattleService) {
        this.typeBattleService = typeBattleService;
    }

    @GetMapping
    public String showForm() {
        log.info("TypeBattleController#showForm");
        return "type-battle";
    }

    @PostMapping("/result")
    public String showResult(
            @RequestParam int idA,
            @RequestParam int idB,
            Model model
    ) {
        log.info("TypeBattleController#showResult idA={} idB={}", idA, idB);

        TypeBattleView view = typeBattleService.compareById(idA, idB);

        model.addAttribute("view", view);
        return "type-battle-result";
    }
}
