package com.dfsek.tectonic.exception;

/**
 * ConfigException wrapper for a reflective access exception.
 */
public class ReflectiveAccessException extends ConfigException {
    public ReflectiveAccessException(String message, ReflectiveOperationException cause) {
        super(message, cause);
    }
}
