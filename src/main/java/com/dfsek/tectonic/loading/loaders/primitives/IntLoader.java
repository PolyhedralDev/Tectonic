package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;

/**
 * Default loader for Integer types.
 */
public class IntLoader implements TypeLoader<Integer> {
    @Override
    public Integer load(AnnotatedType t, Object c, ConfigLoader loader) {
        return ((Number) c).intValue();
    }
}
