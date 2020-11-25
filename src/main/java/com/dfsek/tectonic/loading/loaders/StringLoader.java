package com.dfsek.tectonic.loading.loaders;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

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
