package com.dfsek.polyconfig.exception;

/**
 * Exception thrown when a value is missing from a configuration, and no default value is present.
 */
public class ValueMissingException extends ConfigException {
    private static final long serialVersionUID = 4229997553358413812L;

    public ValueMissingException(String message) {
        super(message);
    }

    public ValueMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
