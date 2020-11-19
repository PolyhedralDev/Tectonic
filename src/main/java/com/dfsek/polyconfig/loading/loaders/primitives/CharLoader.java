package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;

import java.lang.reflect.Type;

public class CharLoader implements TypeLoader<Character> {
    @Override
    public Character load(Type t, Object c, ConfigLoader loader) {
        return (Character) c;
    }
}
