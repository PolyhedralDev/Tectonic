package com.dfsek.tectonic.impl.loading.loaders.primitives;

import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

public class BooleanLoader implements TypeLoader<Boolean> {
    @Override
    public Boolean load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) {
        return (Boolean) c;
    }
}
