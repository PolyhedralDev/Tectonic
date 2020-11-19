package com.dfsek.polyconfig.exception;

/**
 * ConfigException wrapper for a reflective access exception.
 */
public class ReflectiveAccessException extends ConfigException {
    public ReflectiveAccessException(String message, ReflectiveOperationException cause) {
        super(message, cause);
    }
}
