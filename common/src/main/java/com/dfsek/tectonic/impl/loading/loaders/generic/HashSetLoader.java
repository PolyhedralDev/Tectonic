package com.dfsek.tectonic.impl.loading.loaders.generic;

import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.impl.loading.ConfigLoader;
import com.dfsek.tectonic.api.loader.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unchecked")
public class HashSetLoader implements TypeLoader<HashSet<Object>> {
    @Override
    public HashSet<Object> load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) throws LoadException {
        HashSet<Object> set = new HashSet<>();
        if(t instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType pType = (AnnotatedParameterizedType) t;
            AnnotatedType generic = pType.getAnnotatedActualTypeArguments()[0];
            if(c instanceof List) {
                List<Object> objectList = (List<Object>) c;
                for(Object o : objectList) {
                    set.add(loader.loadType(generic, o));
                }
            } else return new HashSet<>(Collections.singleton(loader.loadType(generic, c))); // Singleton
        } else throw new LoadException("Unable to load config");
        return set;
    }
}
