package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;

/**
 * Default loader for Double types.
 */
public class DoubleLoader implements TypeLoader<Double> {
    @Override
    public Double load(AnnotatedType t, Object c, ConfigLoader loader) {
        return ((Number) c).doubleValue();
    }
}
