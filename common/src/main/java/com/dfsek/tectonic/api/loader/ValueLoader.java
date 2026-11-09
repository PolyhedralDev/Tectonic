package com.dfsek.tectonic.api.loader;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.depth.DepthTracker;

import java.lang.reflect.AnnotatedType;


@FunctionalInterface
public interface ValueLoader {
    default Object load(String key, AnnotatedType type, Configuration configuration, DepthTracker depthTracker) {
        return load(key, type, configuration, depthTracker, false);
    }

    Object load(String key, AnnotatedType type, Configuration configuration, DepthTracker depthTracker, boolean isFinal);

    /**
     * Load a value, merging it across the inheritance tree when {@code merge} is set.
     *
     * @param merge Whether the value is {@link com.dfsek.tectonic.api.config.template.annotations.Merge}.
     *
     * @throws UnsupportedOperationException if {@code merge} is set and this loader does not support merging, as the
     *                                       default implementation does not.
     */
    default Object load(String key, AnnotatedType type, Configuration configuration, DepthTracker depthTracker, boolean isFinal,
                        boolean merge) {
        if(merge) throw new UnsupportedOperationException(getClass().getName() + " does not support @Merge values");
        return load(key, type, configuration, depthTracker, isFinal);
    }
}
