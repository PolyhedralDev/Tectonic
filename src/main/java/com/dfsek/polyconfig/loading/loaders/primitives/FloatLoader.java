package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;

import java.lang.reflect.Type;

public class FloatLoader implements TypeLoader<Float> {
    @Override
    public Float load(Type t, Object c, ConfigLoader loader) {
        return (Float) c;
    }
}
