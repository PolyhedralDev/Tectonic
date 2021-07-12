package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;

public class ShortLoader implements TypeLoader<Short> {
    @Override
    public Short load(AnnotatedType t, Object c, ConfigLoader loader) {
        return ((Number) c).shortValue();
    }
}
