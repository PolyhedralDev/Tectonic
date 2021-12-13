package com.dfsek.tectonic.exception;

import com.dfsek.tectonic.api.loader.TypeLoader;

/**
 * Exception thrown when a {@link TypeLoader} fails to load a config
 */
public class LoadException extends ConfigException {
    private static final long serialVersionUID = -186956854213945799L;

    public LoadException(String message) {
        super(message);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
