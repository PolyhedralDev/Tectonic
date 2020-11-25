package com.dfsek.polyconfig.abstraction.exception;

import com.dfsek.polyconfig.exception.ConfigException;

public abstract class AbstractionException extends ConfigException {
    private static final long serialVersionUID = 7100586270889588394L;

    public AbstractionException(String message) {
        super(message);
    }

    public AbstractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
