package com.app.pokemon.controller;

import com.app.pokemon.view.TypeBattleView;
import com.app.pokemon.service.TypeBattleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/type-battle")
public class TypeBattleController {

    private final TypeBattleService typeBattleService;

    public TypeBattleController(TypeBattleService typeBattleService) {
        this.typeBattleService = typeBattleService;
    }

    // 入力画面
    @GetMapping
    public String showForm() {
        return "type-battle";
    }

    // 比較結果
    @PostMapping("/result")
    public String showResult(
            @RequestParam int idA,
            @RequestParam int idB,
            Model model
    ) {
        TypeBattleView view = typeBattleService.compareById(idA, idB);
        model.addAttribute("view", view);
        return "type-battle-result";
    }
}
