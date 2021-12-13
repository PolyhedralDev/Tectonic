package com.dfsek.tectonic.impl.loading.loaders.primitives;

import com.dfsek.tectonic.impl.loading.ConfigLoader;
import com.dfsek.tectonic.api.loader.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

public class CharLoader implements TypeLoader<Character> {
    @Override
    public Character load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) {
        return (Character) c;
    }
}
