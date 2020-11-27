package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;

public class ShortLoader implements TypeLoader<Short> {
    @Override
    public Short load(Type t, Object c, ConfigLoader loader) {
        return ((Number) c).shortValue();
    }
}
