package com.dfsek.polyconfig.abstraction.exception;

import com.dfsek.polyconfig.exception.ConfigException;

public abstract class AbstractionException extends ConfigException {
    public AbstractionException(String message) {
        super(message);
    }

    public AbstractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
