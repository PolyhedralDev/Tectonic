package com.dfsek.polyconfig.exception;

public class ConfigIOException extends ConfigException {
    public ConfigIOException(String message) {
        super(message);
    }

    public ConfigIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
