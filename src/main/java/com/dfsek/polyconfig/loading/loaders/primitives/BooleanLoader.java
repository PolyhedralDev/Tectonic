package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;

public class BooleanLoader implements ClassLoader<Boolean> {
    @Override
    public Boolean load(Type t, Object c, ConfigLoader loader) {
        return (Boolean) c;
    }
}
