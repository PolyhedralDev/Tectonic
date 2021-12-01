package com.dfsek.tectonic.loading.loaders.primitives;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

public class CharLoader implements TypeLoader<Character> {
    @Override
    public Character load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) {
        return (Character) c;
    }
}
