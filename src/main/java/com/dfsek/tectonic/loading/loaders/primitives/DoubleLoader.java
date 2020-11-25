package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;

/**
 * Default loader for Double types.
 */
public class DoubleLoader implements TypeLoader<Double> {
    @Override
    public Double load(Type t, Object c, ConfigLoader loader) {
        return (Double) c;
    }
}
