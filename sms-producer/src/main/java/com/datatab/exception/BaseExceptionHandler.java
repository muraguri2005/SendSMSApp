package com.datatab.exception;

import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
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


    @ExceptionHandler(value = {ResourceAccessException.class})
    public ResponseEntity<?> handleResourceAccessException(ResourceAccessException ex) {
        return new ResponseEntity<>(new ErrorResponse(403, ex.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException ex) {

        if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
            return new ResponseEntity<>(
                    new ErrorResponse(404, "Resource cannot be found, Validate the URL"), HttpStatus.NOT_FOUND);
        } else if (ex.getStatusCode().value() == HttpStatus.FORBIDDEN.value()) {
            return new ResponseEntity<>(new ErrorResponse(403, "Forbidden, Access Denied"),
                    HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new ErrorResponse(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @ExceptionHandler(value = {DataAccessException.class})
    public ResponseEntity<?> handleDataAccessException(DataAccessException ex) {
        return new ResponseEntity<>(new ErrorResponse(500, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {TransactionSystemException.class})
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException ex) {

        Throwable cause = ex.getCause();
        if (cause instanceof RollbackException) {
            Throwable throwable = ex.getCause();
            if (throwable.getCause() instanceof ConstraintViolationException constraintViolationException) {
                HashMap<String, String> errors = new HashMap<>();
                for (ConstraintViolation<?> constraintViolation : constraintViolationException
                        .getConstraintViolations()) {
                    errors.put(constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getMessageTemplate());
                }
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        "Data integrity violation", errors);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ErrorResponse(500, "Internal Server Error"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    protected ResponseEntity<?> handleConflict(ObjectNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex.getExceptionStatus().value(), ex.getMessage()), ex.getExceptionStatus());

    }


    @ExceptionHandler(value = {BaseException.class})
    protected ResponseEntity<?> handleConflict(BaseException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex.getExceptionStatus().value(), ex.getMessage()), ex.getExceptionStatus());

    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<?> handleDataAccessException(NullPointerException ex) {
        return new ResponseEntity<>(new ErrorResponse(500, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleDataAccessException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(500, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
