package com.dfsek.tectonic.loading;

import com.dfsek.tectonic.exception.LoadException;

import java.lang.reflect.Type;

/**
 * Loads a class from an Object retrieved from a config.
 *
 * @param <T> Type to load
 */
public interface TypeLoader<T> {
    T load(Type t, Object c, ConfigLoader loader) throws LoadException;
}
