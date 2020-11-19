package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;

public class LongLoader implements ClassLoader<Long> {
    @Override
    public Long load(Type t, Object c, ConfigLoader loader) {
        return (Long) c;
    }
}
