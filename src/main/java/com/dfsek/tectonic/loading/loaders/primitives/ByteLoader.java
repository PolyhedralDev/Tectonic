package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;

public class ByteLoader implements TypeLoader<Byte> {
    @Override
    public Byte load(Type t, Object c, ConfigLoader loader) {
        return (Byte) c;
    }
}
