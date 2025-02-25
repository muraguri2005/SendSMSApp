package com.datatab.exception;

import java.util.Map;
import java.util.Map.Entry;

public final class ErrorResponse {

    private String message;

    public ErrorResponse(int status, String message) {
        this.setMessage(message);
    }


    public ErrorResponse(int status, String message, Map<String, String> errorMap) {
        this(status, message);
        ErrorHandler eh = new ErrorHandler();
        if (errorMap != null) {
            for (Entry<String, String> error : errorMap.entrySet()) {
                eh.addGeneralError(error.getKey(), error.getValue());
            }
        }
    }


    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }


}
