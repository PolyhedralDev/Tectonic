package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;

/**
 * Default loader for Integer types.
 */
public class IntLoader implements ClassLoader<Integer> {
    @Override
    public Integer load(Type t, Object c, ConfigLoader loader) {
        return (Integer) c;
    }
}
