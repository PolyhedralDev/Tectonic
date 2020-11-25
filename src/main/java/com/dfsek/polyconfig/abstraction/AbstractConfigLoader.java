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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractConfigLoader implements TypeRegistry {
    private final ConfigLoader delegate = new ConfigLoader();

    @Override
    public TypeRegistry registerLoader(Type t, TypeLoader<?> loader) {
        delegate.registerLoader(t, loader);
        return this;
    }

    public <E extends ConfigTemplate> List<E> load(List<InputStream> inputStreams, TemplateProvider provider) throws ConfigException {
        AbstractPool pool = new AbstractPool();
        List<Configuration> configurations = new ArrayList<>();

        for(InputStream stream : inputStreams) {
            configurations.add(new Configuration(stream));
        }

        Map<String, Configuration> configurationMap = new HashMap<>();
        for(Configuration cfg : configurations) {
            Prototype p = new Prototype();
            delegate.load(p, cfg);
            configurationMap.put(p.getId(), cfg);
            pool.add(p);
        }
        pool.loadAll();

        List<E> fnlList = new ArrayList<>();

        for(Prototype p : pool.getPrototypes()) { // Load root configs.
            Prototype current = p;

            do {
                current = p.getParent();
            } while(!current.isRoot());


            if(p.isRoot()) {
                ConfigTemplate c = provider.getInstance();
                delegate.load(c, configurationMap.get(p.getId()));
                p.setConfig(c);
            }
        }

        return null;
    }

    private static final class Pair<A, B> {
        private final A a;
        private final B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getA() {
            return a;
        }

        public B getB() {
            return b;
        }
    }
}