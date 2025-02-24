package com.datatab.exception;


import java.io.Serial;

public class ObjectNotFoundException extends BaseException {


    @Serial
    private static final long serialVersionUID = -8267428407854800585L;

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
