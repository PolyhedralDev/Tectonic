package com.dfsek.tectonic.loading.loaders.generic;

import com.dfsek.tectonic.annotations.Merge;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class HashMapLoader implements TypeLoader<Map<Object, Object>> {
    @Override
    public Map<Object, Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<Object, Object> config = (Map<Object, Object>) c;
        HashMap<Object, Object> map = new HashMap<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type key = pType.getActualTypeArguments()[0];
            Type value = pType.getActualTypeArguments()[1];
            for(Map.Entry<Object, Object> entry : config.entrySet()) {
                if(entry.getKey().equals("_SUPER")) continue;
                map.put(loader.loadType(key, entry.getKey()), loader.loadType(value, entry.getValue()));
            }
        } else throw new LoadException("Unable to load config");

        return map;
    }

    @Override
    public Map<Object, Object> loadMerged(Type t, Object rootConfigSection, ConfigLoader loader, Object parent, Merge.Type merge) throws LoadException {
        Map<Object, Object> child = load(t, rootConfigSection, loader);
        if(merge.equals(Merge.Type.ALWAYS) || ((Map<Object, Object>) rootConfigSection).containsKey("_SUPER")) {
            if(parent == null) return child;
            Map<Object, Object> parentMap = load(t, parent, loader);
            parentMap.putAll(child); // Child should overwrite parent
            return parentMap;
        }
        return child;
    }
}
