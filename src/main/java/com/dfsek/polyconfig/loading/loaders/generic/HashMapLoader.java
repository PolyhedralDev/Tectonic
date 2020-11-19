package com.dfsek.polyconfig.loading.loaders.generic;

import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class HashMapLoader implements ClassLoader<HashMap<Object, Object>> {
    @Override
    public HashMap<Object, Object> load(Type t, Object c, ConfigLoader loader) {
        System.out.println("fsdfgjhsdfgkjdhfgdjfhg");
        Map<String, Object> config = (Map<String, Object>) c;
        HashMap<Object, Object> map = new HashMap<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type key = pType.getActualTypeArguments()[0];
            Type value = pType.getActualTypeArguments()[1];
            for(Map.Entry<String, Object> entry : config.entrySet()) {
                map.put(loader.loadType(key, entry.getKey()), loader.loadType(value, entry.getValue()));
            }
        } else throw new IllegalArgumentException(); // TODO: Dedicated exception

        return map;
    }
}
