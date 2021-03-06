package com.dfsek.tectonic.loading.loaders.generic;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class HashMapLoader implements TypeLoader<HashMap<Object, Object>> {
    @Override
    public HashMap<Object, Object> load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> config = (Map<String, Object>) c;
        HashMap<Object, Object> map = new HashMap<>();
        if(t instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType pType = (AnnotatedParameterizedType) t;
            AnnotatedType key = pType.getAnnotatedActualTypeArguments()[0];
            AnnotatedType value = pType.getAnnotatedActualTypeArguments()[1];
            for(Map.Entry<String, Object> entry : config.entrySet()) {
                map.put(loader.loadType(key, entry.getKey()), loader.loadType(value, entry.getValue()));
            }
        } else throw new LoadException("Unable to load config");

        return map;
    }
}
