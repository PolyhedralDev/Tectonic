package com.dfsek.polyconfig.loading;

import java.lang.reflect.Type;

/**
 * Loads a class from an Object retrieved from a config.
 *
 * @param <T> Type to load
 */
public interface ClassLoader<T> {
    T load(Type t, Object c, ConfigLoader loader);
}
