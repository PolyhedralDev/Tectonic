package com.dfsek.tectonic.loading.loaders.generic;

import com.dfsek.tectonic.annotations.Merge;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassLoader for ArrayLists. Permits any type parameters, and will use the ConfigLoader's registered type loader for
 * any parameters.
 */
@SuppressWarnings("unchecked")
public class ArrayListLoader implements TypeLoader<List<Object>> {
    @Override
    public List<Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        List<Object> objectList = (List<Object>) c;
        List<Object> list = new ArrayList<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type generic = pType.getActualTypeArguments()[0];
            for(Object o : objectList) {
                if(o.equals("_SUPER")) continue;
                list.add(loader.loadType(generic, o));
            }
        } else throw new LoadException("Unable to load config");
        return list;
    }

    @Override
    public List<Object> loadMerged(Type t, Object rootConfigSection, ConfigLoader loader, Object parent, Merge.Type merge) throws LoadException {
        List<Object> child = load(t, rootConfigSection, loader);
        if(merge.equals(Merge.Type.ALWAYS) || ((List<Object>) rootConfigSection).contains("_SUPER")) {
            if(parent == null) return child;
            List<Object> parentList = load(t, parent, loader);
            parentList.addAll(child);
            return parentList;
        }
        return child;
    }
}
