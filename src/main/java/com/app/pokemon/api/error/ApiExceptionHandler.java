package com.app.pokemon.api.error;

import com.app.pokemon.controller.PokedexController;
import com.app.pokemon.controller.SvBattleController;
import com.app.pokemon.controller.TypeBattleController;
import com.app.pokemon.exception.BadRequestException;
import com.app.pokemon.exception.ExternalApiException;
import com.app.pokemon.exception.MoveNotFoundException;
import com.app.pokemon.exception.PokemonNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = {SvBattleController.class, TypeBattleController.class })

public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException e, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e.getMessage(), req);
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    public ResponseEntity<ApiError> handlePokemonNotFound(PokemonNotFoundException e, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "POKEMON_NOT_FOUND", e.getMessage(), req);
    }

    @ExceptionHandler(MoveNotFoundException.class)
    public ResponseEntity<ApiError> handleMoveNotFound(MoveNotFoundException e, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "MOVE_NOT_FOUND", e.getMessage(), req);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ApiError> handleExternalApi(ExternalApiException e, HttpServletRequest req) {
        return build(HttpStatus.BAD_GATEWAY, "EXTERNAL_API_ERROR", e.getMessage(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception e, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "予期しないエラーが発生しました", req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message, HttpServletRequest req) {
        String requestId = MDC.get("requestId");
        ApiError body = new ApiError(
                code,
                message,
                req.getRequestURI(),
                requestId,
                OffsetDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, req);
    }

    private String formatFieldError(FieldError fe) {
        String field = fe.getField();
        String defaultMessage = fe.getDefaultMessage();
        return defaultMessage == null ? field + " is invalid" : field + ": " + defaultMessage;
    }

}
