package com.dfsek.polyconfig.loading.loaders.primitives;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;

import java.lang.reflect.Type;

public class ByteLoader implements TypeLoader<Byte> {
    @Override
    public Byte load(Type t, Object c, ConfigLoader loader) {
        return (Byte) c;
    }
}
