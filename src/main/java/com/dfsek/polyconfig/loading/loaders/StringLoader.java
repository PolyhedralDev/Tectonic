package com.dfsek.polyconfig.loading.loaders;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;

/**
 * Default loader for String type.
 */
public class StringLoader implements ClassLoader<String> {
    @Override
    public String load(Type t, Object c, ConfigLoader loader) {
        return (String) c;
    }
}
