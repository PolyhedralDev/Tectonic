package com.dfsek.tectonic.impl.loading.loaders.primitives;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;


public class FloatLoader implements TypeLoader<Float> {
    @Override
    public Float load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) {
        try {
            return ((Number) c).floatValue();
        } catch(ClassCastException e) {
            throw new LoadException("Data provided is not a float. Data is type: " + c.getClass().getSimpleName(), e, depthTracker);
        }
    }
}
