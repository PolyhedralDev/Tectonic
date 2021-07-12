package com.dfsek.tectonic.exception;

/**
 * ConfigException wrapper for a reflective access exception.
 */
public class ReflectiveAccessException extends ConfigException {
    private static final long serialVersionUID = -2960519562022569730L;

    public ReflectiveAccessException(String message, ReflectiveOperationException cause) {
        super(message, cause);
    }
}
