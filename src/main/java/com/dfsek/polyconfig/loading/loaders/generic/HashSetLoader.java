package com.dfsek.polyconfig.loading.loaders.generic;

import com.dfsek.polyconfig.exception.LoadException;
import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unchecked")
public class HashSetLoader implements TypeLoader<HashSet<Object>> {
    @Override
    public HashSet<Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        List<Object> objectList = (List<Object>) c;
        HashSet<Object> set = new HashSet<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type generic = pType.getActualTypeArguments()[0];
            for(Object o : objectList) {
                set.add(loader.loadType(generic, o));
            }
        } else throw new LoadException("Unable to load config");
        return set;
    }
}
