package com.dfsek.tectonic.api.exception;

import com.dfsek.tectonic.api.depth.DepthTracker;

/**
 * ConfigException wrapper for a reflective access exception.
 */
public class ReflectiveAccessException extends RuntimeException {
    private static final long serialVersionUID = -2960519562022569730L;

    public ReflectiveAccessException(String message, ReflectiveOperationException cause) {
        super(message, cause);
    }
}
