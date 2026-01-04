package com.app.pokemon.controller;

import com.app.pokemon.api.FullBattleRequest;
import com.app.pokemon.api.FullBattleResponse;
import com.app.pokemon.service.SvBattleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sv-battle")
public class SvBattleController {

    private final SvBattleService svBattleService;

    public SvBattleController(SvBattleService svBattleService) {
        this.svBattleService = svBattleService;
    }

    @PostMapping(
            value = "/full",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FullBattleResponse fullBattle(@RequestBody FullBattleRequest req) {
        return svBattleService.fullBattle(req);
    }
}
