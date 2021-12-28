package com.dfsek.tectonic.api.exception;

import com.dfsek.tectonic.api.depth.DepthTracker;

public class InvalidTemplateException extends ConfigException {
    public InvalidTemplateException(String message, DepthTracker tracker) {
        super(message, tracker);
    }
}
