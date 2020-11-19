package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;

public class CharLoader implements ClassLoader<Character> {
    @Override
    public Character load(Type t, Object c, ConfigLoader loader) {
        return (Character) c;
    }
}
