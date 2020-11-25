package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;

public class FloatLoader implements TypeLoader<Float> {
    @Override
    public Float load(Type t, Object c, ConfigLoader loader) {
        return (Float) c;
    }
}
