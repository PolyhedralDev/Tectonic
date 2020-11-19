package com.dfsek.polyconfig.loading.loaders.generic;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassLoader for ArrayLists. Permits any type parameters, and will use the ConfigLoader's registered type loader for
 * any parameters.
 */
@SuppressWarnings("unchecked")
public class ArrayListLoader implements ClassLoader<ArrayList<Object>> {
    @Override
    public ArrayList<Object> load(Type t, Object c, ConfigLoader loader) {
        List<Object> objectList = (List<Object>) c;
        ArrayList<Object> list = new ArrayList<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type generic = pType.getActualTypeArguments()[0];
            for(Object o : objectList) {
                list.add(loader.loadType(generic, o));
            }
        } else throw new IllegalArgumentException(); // TODO: Dedicated exception
        return list;
    }
}
