package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;

/**
 * Default loader for Integer types.
 */
public class IntLoader implements TypeLoader<Integer> {
    @Override
    public Integer load(Type t, Object c, ConfigLoader loader) {
        return (Integer) c;
    }
}
