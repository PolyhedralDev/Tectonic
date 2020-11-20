package com.dfsek.polyconfig.abstraction;

import com.dfsek.polyconfig.ConfigTemplate;
import com.dfsek.polyconfig.Configuration;
import com.dfsek.polyconfig.exception.ConfigException;
import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;
import com.dfsek.polyconfig.loading.TypeRegistry;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AbstractConfigLoader implements TypeRegistry {
    private final ConfigLoader delegate = new ConfigLoader();

    @Override
    public TypeRegistry registerLoader(Type t, TypeLoader<?> loader) {
        delegate.registerLoader(t, loader);
        return this;
    }

    public <E extends ConfigTemplate> List<E> load(List<InputStream> inputStreams, Class<E> clazz) throws ConfigException {
        AbstractPool pool = new AbstractPool();
        List<Configuration> configurations = new ArrayList<>();
        for(InputStream stream : inputStreams) {
            configurations.add(new Configuration(stream));
        }
        for(Configuration cfg : configurations) {
            Prototype p = new Prototype();
            delegate.load(p, cfg);
            pool.add(p);
        }
        pool.loadAll();
        return null;
    }
}