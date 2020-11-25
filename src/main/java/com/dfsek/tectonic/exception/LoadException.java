package com.dfsek.tectonic.exception;

/**
 * Exception thrown when a {@link com.dfsek.tectonic.loading.TypeLoader} fails to load a config
 */
public class LoadException extends ConfigException {
    public LoadException(String message) {
        super(message);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
