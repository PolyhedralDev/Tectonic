package com.dfsek.tectonic.api.exception.abstraction;

/**
 * Thrown when a parent (super) config cannot be found.
 */
public class ParentNotFoundException extends AbstractionException {
    private static final long serialVersionUID = 6754141573771927677L;

    public ParentNotFoundException(String message) {
        super(message);
    }

    public ParentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
