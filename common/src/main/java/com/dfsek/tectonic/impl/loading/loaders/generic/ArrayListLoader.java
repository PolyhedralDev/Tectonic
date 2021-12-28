package com.dfsek.tectonic.impl.loading.loaders.generic;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ClassLoader for ArrayLists. Permits any type parameters, and will use the ConfigLoader's registered type loader for
 * any parameters.
 */
@SuppressWarnings("unchecked")
public class ArrayListLoader implements TypeLoader<ArrayList<Object>> {
    @Override
    public ArrayList<Object> load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) throws LoadException {
        ArrayList<Object> list = new ArrayList<>();
        if(t instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType pType = (AnnotatedParameterizedType) t;
            AnnotatedType generic = pType.getAnnotatedActualTypeArguments()[0];
            if(c instanceof List) {
                List<Object> objectList = (List<Object>) c;
                for(int i = 0; i < objectList.size(); i++) {
                    Object o = objectList.get(i);
                    list.add(loader.loadType(generic, o, depthTracker.index(i)));
                }
            } else return new ArrayList<>(Collections.singleton(loader.loadType(generic, c, depthTracker.index(0)))); // Singleton
        } else throw new LoadException("Unable to load config", depthTracker);
        return list;
    }
}
