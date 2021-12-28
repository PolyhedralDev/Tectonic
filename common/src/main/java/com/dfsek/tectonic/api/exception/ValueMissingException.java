package com.dfsek.tectonic.api.exception;

import com.dfsek.tectonic.api.depth.DepthTracker;

/**
 * Exception thrown when a value is missing from a configuration, and no default value is present.
 */
public class ValueMissingException extends ConfigException {
    private static final long serialVersionUID = 4229997553358413812L;

    public ValueMissingException(String message, DepthTracker tracker) {
        super(message, tracker);
    }

    public ValueMissingException(String message, Throwable cause, DepthTracker tracker) {
        super(message, cause, tracker);
    }
}
