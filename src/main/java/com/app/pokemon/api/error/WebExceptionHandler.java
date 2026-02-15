package com.app.pokemon.api.error;

import com.app.pokemon.controller.HomeController;
import com.app.pokemon.controller.PokedexController;
import com.app.pokemon.controller.SvBattleViewController;
import com.app.pokemon.exception.MoveNotFoundException;
import com.app.pokemon.exception.PokemonNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = { PokedexController.class, HomeController.class, SvBattleViewController.class })
public class WebExceptionHandler {

    @ExceptionHandler(PokemonNotFoundException.class)
    public String handlePokemonNotFound(PokemonNotFoundException e, Model model, HttpServletRequest req) {
        model.addAttribute("pokemonId", e.getId());
        return buildErrorView(model, req, "ポケモンが見つかりません", "error/not-found");
    }

    @ExceptionHandler(MoveNotFoundException.class)
    public String handleMoveNotFound(MoveNotFoundException e, Model model, HttpServletRequest req) {
        model.addAttribute("errorMessage", e.getMessage());
        return buildErrorView(model, req, "技が見つかりません", "error/not-found");
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception e, Model model, HttpServletRequest req) {
        model.addAttribute("errorMessage", "予期しないエラーが発生しました");
        return buildErrorView(model, req, "エラーが発生しました", "error/general");
    }

    private String buildErrorView(Model model, HttpServletRequest req, String title, String viewName) {
        model.addAttribute("errorTitle", title);
        model.addAttribute("path", req.getRequestURI());
        return viewName;
    }
}
