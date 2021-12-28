package com.dfsek.tectonic.api.exception;

import com.dfsek.tectonic.api.depth.DepthTracker;

public abstract class ConfigException extends RuntimeException {
    private static final long serialVersionUID = 1316896835740257026L;

    public ConfigException(String message, DepthTracker tracker) {
        super(createMessage(message, tracker));
    }

    private static String createMessage(String message, DepthTracker tracker) {
        return "Failed to load configuration:" +
                "\n\n\tConfiguration: " + tracker.getConfigurationName() +
                "\n\tMessage: " + message +
                "\n\tPath: " + tracker.pathDescriptor().replace("\n", "\n\t") +
                "\n\tFull Path: " + tracker.verbosePathDescriptor().replace("\n", "\n\t") +
                "\n";
    }

    public ConfigException(String message, Throwable cause, DepthTracker tracker) {
        super(createMessage(message, tracker), cause);
    }
}
