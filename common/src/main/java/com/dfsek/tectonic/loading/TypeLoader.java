package com.dfsek.tectonic.loading;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.util.ClassAnnotatedTypeImpl;

import java.lang.reflect.AnnotatedType;

/**
 * Loads a class from an Object retrieved from a config.
 *
 * @param <T> Type to load
 */
public interface TypeLoader<T> {
    T load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException;
    default T load(Class<T> t, Object c, ConfigLoader loader) throws LoadException {
        return load(new ClassAnnotatedTypeImpl(t), c, loader);
    }
}
