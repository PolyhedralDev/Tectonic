package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;

public class ByteLoader implements TypeLoader<Byte> {
    @Override
    public Byte load(AnnotatedType t, Object c, ConfigLoader loader) {
        return ((Number) c).byteValue();
    }
}
