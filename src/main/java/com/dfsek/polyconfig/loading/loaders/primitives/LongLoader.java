package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;

import java.lang.reflect.Type;

public class LongLoader implements TypeLoader<Long> {
    @Override
    public Long load(Type t, Object c, ConfigLoader loader) {
        return (Long) c;
    }
}
