package com.dfsek.tectonic.api.exception;

public class InvalidTemplateException extends ConfigException {
    public InvalidTemplateException(String message) {
        super(message);
    }

    public InvalidTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
