package com.dfsek.tectonic.api.exception;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.depth.DepthTracker;

/**
 * Thrown when validation fails on a {@link ValidatedConfigTemplate}.
 */
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 2913531776381159021L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
