package com.datatab.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    @NonNull
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull ServerWebExchange exchange) {
        Map<String, String> errorMap = new HashMap<>();

        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        errors.forEach(err -> {
            FieldError field = (FieldError) err;
            errorMap.put(field.getField(), field.getDefaultMessage());
        });

        return Mono.just(new ResponseEntity<>(new ErrorResponse(400, "Sorry, validation errors occurred", errorMap),
                HttpStatus.BAD_REQUEST));
    }

    @Override
    @NonNull
    protected Mono<ResponseEntity<Object>> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull ServerWebExchange exchange) {
        return Mono.just(new ResponseEntity<>(new ErrorResponse(500, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    @NonNull
    protected Mono<ResponseEntity<Object>> handleServerWebInputException(ServerWebInputException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull ServerWebExchange exchange) {
        return Mono.just(new ResponseEntity<>(new ErrorResponse(400, ex.getMessage()),
                HttpStatus.BAD_REQUEST));
    }
}
