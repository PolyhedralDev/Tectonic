package com.dfsek.polyconfig.abstraction.exception;

/**
 * Thrown when circular inheritance is detected.
 */
public class CircularInheritanceException extends AbstractionException {
    public CircularInheritanceException(String message) {
        super(message);
    }

    public CircularInheritanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
