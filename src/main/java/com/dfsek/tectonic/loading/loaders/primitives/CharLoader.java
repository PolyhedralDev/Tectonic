package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;

public class CharLoader implements TypeLoader<Character> {
    @Override
    public Character load(Type t, Object c, ConfigLoader loader) {
        return (Character) c;
    }
}
