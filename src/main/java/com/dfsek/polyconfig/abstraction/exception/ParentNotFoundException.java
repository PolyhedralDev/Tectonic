package com.dfsek.polyconfig.abstraction.exception;

/**
 * Thrown when a parent (super) config cannot be found.
 */
public class ParentNotFoundException extends AbstractionException {
    public ParentNotFoundException(String message) {
        super(message);
    }

    public ParentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
