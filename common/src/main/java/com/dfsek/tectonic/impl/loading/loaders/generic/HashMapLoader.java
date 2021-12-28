package com.dfsek.tectonic.impl.loading.loaders.generic;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class HashMapLoader implements TypeLoader<HashMap<Object, Object>> {
    @Override
    public HashMap<Object, Object> load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) throws LoadException {
        Map<String, Object> config = (Map<String, Object>) c;
        HashMap<Object, Object> map = new HashMap<>();
        if(t instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType pType = (AnnotatedParameterizedType) t;
            AnnotatedType key = pType.getAnnotatedActualTypeArguments()[0];
            AnnotatedType value = pType.getAnnotatedActualTypeArguments()[1];
            for(Map.Entry<String, Object> entry : config.entrySet()) {
                Object loadedKey = loader.loadType(key, entry.getKey(), depthTracker.entry(entry.getKey()));
                Object loadedValue = loader.loadType(value, entry.getValue(), depthTracker.entry(entry.getKey()));
                map.put(loadedKey, loadedValue);
            }
        } else throw new LoadException("Unable to load config", depthTracker);

        return map;
    }
}
