package com.dfsek.tectonic.impl.loading.object;

import com.dfsek.tectonic.api.ObjectTemplate;
import com.dfsek.tectonic.impl.MapConfiguration;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class ObjectTemplateLoader<T> implements TypeLoader<T> {
    private final Supplier<ObjectTemplate<T>> provider;

    public ObjectTemplateLoader(Supplier<ObjectTemplate<T>> provider) {
        this.provider = provider;
    }

    @Override
    public T load(@NotNull AnnotatedType t, @NotNull Object c, ConfigLoader loader) throws LoadException {
        ObjectTemplate<T> template = provider.get();
        try {
            loader.load(template, new MapConfiguration((Map<String, Object>) c));
        } catch(ConfigException e) {
            throw new LoadException("Unable to load object.", e);
        }
        return template.get();
    }
}
