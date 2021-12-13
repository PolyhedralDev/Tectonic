package com.dfsek.tectonic.api.exception.abstraction;

/**
 * Thrown when circular inheritance is detected.
 */
public class CircularInheritanceException extends AbstractionException {
    private static final long serialVersionUID = -6801324511915680767L;

    public CircularInheritanceException(String message) {
        super(message);
    }

    public CircularInheritanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
