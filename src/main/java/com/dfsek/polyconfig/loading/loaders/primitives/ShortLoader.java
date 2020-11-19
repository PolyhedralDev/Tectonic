package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;

public class ShortLoader implements ClassLoader<Short> {
    @Override
    public Short load(Type t, Object c, ConfigLoader loader) {
        return (Short) c;
    }
}
