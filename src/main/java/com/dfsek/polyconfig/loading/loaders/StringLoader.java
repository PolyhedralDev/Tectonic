package com.dfsek.polyconfig.loading.loaders;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;

import java.lang.reflect.Type;

/**
 * Default loader for String type.
 */
public class StringLoader implements TypeLoader<String> {
    @Override
    public String load(Type t, Object c, ConfigLoader loader) {
        return (String) c;
    }
}
