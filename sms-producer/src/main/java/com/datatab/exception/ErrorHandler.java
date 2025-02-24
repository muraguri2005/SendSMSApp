package com.datatab.exception;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class ErrorHandler implements Serializable {

    @Serial
    private static final long serialVersionUID = -6030757106459377231L;
    private final Map<String, Map<String, String>> errors;

    public ErrorHandler() {
        errors = new HashMap<>();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private Map<String, String> getNamespaceErrorMap(String namespace) {
        Map<String, String> map = errors.get(namespace);
        if (map == null) {
            map = new HashMap<>();
        }
        errors.put(namespace, map);
        return map;
    }

    public void addGeneralError(String property, String message) {
        getNamespaceErrorMap("error").put(property, message);

    }


    public Map<String, Map<String, String>> getErrors() {
        return errors;
    }


}
