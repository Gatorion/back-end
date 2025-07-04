package com.gatorion.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * Este método será chamado sempre que uma RuntimeException for lançada
     * em qualquer um dos controllers.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        // Nós podemos customizar o corpo do erro para ser um JSON claro
        Map<String, Object> body = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Recurso não encontrado",
                "message", ex.getMessage()
        );

        // Retorna o status 404 Not Found com a mensagem de erro no corpo
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}