package com.dfsek.tectonic.exception;

/**
 * Thrown when validation fails on a {@link com.dfsek.tectonic.config.ValidatedConfigTemplate}.
 */
public class ValidationException extends ConfigException {
    private static final long serialVersionUID = 2913531776381159021L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
