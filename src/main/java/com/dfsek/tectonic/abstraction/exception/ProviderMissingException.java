package com.dfsek.tectonic.abstraction.exception;

/**
 * Thrown when an abstract value is loaded with no AbstractValueProvider given.
 */
public class ProviderMissingException extends AbstractionException {
    private static final long serialVersionUID = 880669554513044509L;

    public ProviderMissingException(String message) {
        super(message);
    }

    public ProviderMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
