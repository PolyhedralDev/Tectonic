package com.dfsek.tectonic.api.loader.type;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.util.ClassAnnotatedTypeImpl;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

/**
 * Loads a class from an Object retrieved from a config.
 *
 * @param <T> Type to load
 */
public interface TypeLoader<T> {
    T load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) throws LoadException;

    default T load(@NotNull Class<T> t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) throws LoadException {
        return load(new ClassAnnotatedTypeImpl(t), c, loader, depthTracker);
    }
}
