package com.dfsek.tectonic.loading.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class EnumLoader implements TypeLoader<Enum<?>> {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Enum<?> load(AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) throws LoadException {
        Class<Enum> enumClass;

        Type base = t.getType();

        if(base instanceof ParameterizedType) {
            enumClass = (Class<Enum>) ((ParameterizedType) base).getRawType();
        } else {
            enumClass = (Class<Enum>) base;
        }

        return Enum.valueOf(enumClass, (String) c);
    }
}
