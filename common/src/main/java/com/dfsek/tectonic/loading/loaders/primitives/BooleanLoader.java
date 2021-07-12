package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;

public class BooleanLoader implements TypeLoader<Boolean> {
    @Override
    public Boolean load(AnnotatedType t, Object c, ConfigLoader loader) {
        return (Boolean) c;
    }
}
