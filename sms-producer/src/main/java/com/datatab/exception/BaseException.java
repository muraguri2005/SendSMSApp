package com.datatab.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class BaseException extends Exception {

    @Serial
    private static final long serialVersionUID = -854814840218996125L;

    public BaseException(String message) {
        super(message);
    }

    public HttpStatus getExceptionStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
