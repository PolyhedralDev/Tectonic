package com.dfsek.tectonic.exception;

public class ConfigIOException extends ConfigException {
    private static final long serialVersionUID = -7617584453224503966L;

    public ConfigIOException(String message) {
        super(message);
    }

    public ConfigIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
