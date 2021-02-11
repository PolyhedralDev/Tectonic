package com.dfsek.tectonic.loading.loaders.generic;

import com.dfsek.tectonic.annotations.Merge;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class HashSetLoader implements TypeLoader<Set<Object>> {
    @Override
    public Set<Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        List<Object> objectList = (List<Object>) c;
        Set<Object> set = new HashSet<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type generic = pType.getActualTypeArguments()[0];
            for(Object o : objectList) {
                if(o.equals("_SUPER")) continue;
                set.add(loader.loadType(generic, o));
            }
        } else throw new LoadException("Unable to load config");
        return set;
    }

    @Override
    public Set<Object> loadMerged(Type t, Object rootConfigSection, ConfigLoader loader, Object parent, Merge.Type merge) throws LoadException {
        Set<Object> child = load(t, rootConfigSection, loader);
        if(merge.equals(Merge.Type.ALWAYS) || ((List<Object>) rootConfigSection).contains("_SUPER")) {
            if(parent == null) return child;
            Set<Object> parentList = load(t, parent, loader);
            parentList.addAll(child);
            return parentList;
        }
        return child;
    }
}
