package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;

public class CharLoader implements TypeLoader<Character> {
    @Override
    public Character load(AnnotatedType t, Object c, ConfigLoader loader) {
        return (Character) c;
    }
}
