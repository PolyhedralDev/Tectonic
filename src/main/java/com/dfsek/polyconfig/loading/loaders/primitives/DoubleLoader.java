package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;

/**
 * Default loader for Double types.
 */
public class DoubleLoader implements ClassLoader<Double> {
    @Override
    public Double load(Type t, Object c, ConfigLoader loader) {
        return (Double) c;
    }
}
