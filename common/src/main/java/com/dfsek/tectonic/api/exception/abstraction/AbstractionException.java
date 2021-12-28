package com.dfsek.tectonic.api.exception.abstraction;

import com.dfsek.tectonic.api.exception.ConfigException;

public abstract class AbstractionException extends RuntimeException {
    private static final long serialVersionUID = 7100586270889588394L;

    public AbstractionException(String message) {
        super(message);
    }

    public AbstractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
