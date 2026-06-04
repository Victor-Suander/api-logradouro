package com.victors.apilogradouro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Centraliza o tratamento de exceções lançadas pelos controllers.
 * @RestControllerAdvice intercepta automaticamente qualquer exceção não tratada
 * em todos os @RestController da aplicação e retorna respostas HTTP padronizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Trata violações de regra de negócio (ex: sigla duplicada). Retorna HTTP 400 Bad Request. */
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> handleRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensagem", ex.getMessage()));
    }

    /** Trata recursos não encontrados no banco (ex: UF com id inexistente). Retorna HTTP 404 Not Found. */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensagem", ex.getMessage()));
    }
}