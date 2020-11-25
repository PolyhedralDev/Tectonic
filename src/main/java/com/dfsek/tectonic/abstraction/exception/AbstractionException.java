package com.dfsek.tectonic.abstraction.exception;

import com.dfsek.tectonic.exception.ConfigException;

public abstract class AbstractionException extends ConfigException {
    private static final long serialVersionUID = 7100586270889588394L;

    public AbstractionException(String message) {
        super(message);
    }

    public AbstractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
