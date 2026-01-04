package com.app.pokemon.controller;

import com.app.pokemon.service.TypeBattleService;
import com.app.pokemon.view.TypeBattleView;
import jakarta.servlet.http.HttpServletRequest;
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
    public String showForm(HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        log.info("TypeBattleController#showForm requestId={}", requestId);
        return "type-battle";
    }

    @PostMapping("/result")
    public String showResult(
            @RequestParam int idA,
            @RequestParam int idB,
            Model model,
            HttpServletRequest request
    ) {
        String requestId = (String) request.getAttribute("requestId");
        log.info("TypeBattleController#showResult requestId={} idA={} idB={}", requestId, idA, idB);

        TypeBattleView view = typeBattleService.compareById(idA, idB, requestId);

        model.addAttribute("view", view);
        return "type-battle-result";
    }
}
