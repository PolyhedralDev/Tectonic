package com.dfsek.tectonic.loading.loaders.generic;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
    public ArrayList<Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        ArrayList<Object> list = new ArrayList<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type generic = pType.getActualTypeArguments()[0];
            if(c instanceof List) {
                List<Object> objectList = (List<Object>) c;
                for(Object o : objectList) {
                    list.add(loader.loadType(generic, o));
                }
            } else return new ArrayList<>(Collections.singleton(loader.loadType(generic, c))); // Singleton
        } else throw new LoadException("Unable to load config");
        return list;
    }
}
