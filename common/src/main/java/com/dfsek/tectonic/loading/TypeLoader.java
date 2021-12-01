package com.dfsek.tectonic.loading;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.util.ClassAnnotatedTypeImpl;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

/**
 * Loads a class from an Object retrieved from a config.
 *
 * @param <T> Type to load
 */
public interface TypeLoader<T> {
    T load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) throws LoadException;

    default T load(@NotNull Class<T> t, @NotNull Object c, @NotNull ConfigLoader loader) throws LoadException {
        return load(new ClassAnnotatedTypeImpl(t), c, loader);
    }
}
