package com.agencia.viagens.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tratamento global de exceções da API.
 * Retorna respostas JSON padronizadas para todos os erros.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----------------------------------------------------------------
    // 404 — Destino não encontrado
    // ----------------------------------------------------------------

    @ExceptionHandler(DestinoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDestinoNotFound(
            DestinoNotFoundException ex) {

        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    // ----------------------------------------------------------------
    // 400 — Erros de validação do Bean Validation (@Valid)
    // ----------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valor inválido",
                        (first, second) -> first
                ));

        Map<String, Object> body = buildBaseResponse(HttpStatus.BAD_REQUEST, "Erro de validação");
        body.put("campos", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    // ----------------------------------------------------------------
    // 400 — Tipo errado no path variable (ex.: letras onde se espera Long)
    // ----------------------------------------------------------------

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String mensagem = String.format(
                "O parâmetro '%s' deve ser do tipo %s.",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido"
        );
        return buildResponse(HttpStatus.BAD_REQUEST, "Parâmetro inválido", mensagem);
    }

    // ----------------------------------------------------------------
    // 500 — Qualquer outra exceção não tratada
    // ----------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String erro, String mensagem) {

        Map<String, Object> body = buildBaseResponse(status, erro);
        body.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(body);
    }

    private Map<String, Object> buildBaseResponse(HttpStatus status, String erro) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("erro", erro);
        return body;
    }
}
