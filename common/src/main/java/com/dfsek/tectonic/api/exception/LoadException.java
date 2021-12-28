package com.dfsek.tectonic.api.exception;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

/**
 * Exception thrown when a {@link TypeLoader} fails to load a config
 */
public class LoadException extends ConfigException {
    private static final long serialVersionUID = -186956854213945799L;

    public LoadException(String message, DepthTracker tracker) {
        super(message, tracker);
    }

    public LoadException(String message, Throwable cause, DepthTracker tracker) {
        super(message, cause, tracker);
    }
}
