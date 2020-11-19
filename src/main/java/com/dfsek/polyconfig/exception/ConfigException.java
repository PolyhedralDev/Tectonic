package com.dfsek.polyconfig.exception;

public abstract class ConfigException extends Exception {
    private static final long serialVersionUID = 1316896835740257026L;

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
