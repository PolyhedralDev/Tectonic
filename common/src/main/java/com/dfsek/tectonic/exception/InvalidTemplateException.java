package com.dfsek.tectonic.exception;

public class InvalidTemplateException extends ConfigException {
    public InvalidTemplateException(String message) {
        super(message);
    }

    public InvalidTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
