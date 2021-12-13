package com.dfsek.tectonic.api.loader;

import com.dfsek.tectonic.api.config.Configuration;

import java.lang.reflect.AnnotatedType;

@FunctionalInterface
public interface ValueLoader {
    default Object load(String key, AnnotatedType type, Configuration configuration) {
        return load(key, type, configuration, false);
    }

    Object load(String key, AnnotatedType type, Configuration configuration, boolean isFinal);
}
