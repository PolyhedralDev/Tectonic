package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;

import java.lang.reflect.Type;

public class BooleanLoader implements TypeLoader<Boolean> {
    @Override
    public Boolean load(Type t, Object c, ConfigLoader loader) {
        return (Boolean) c;
    }
}
