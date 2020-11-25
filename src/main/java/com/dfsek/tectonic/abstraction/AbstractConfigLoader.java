package com.dfsek.tectonic.abstraction;

import com.dfsek.tectonic.ConfigTemplate;
import com.dfsek.tectonic.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.TypeRegistry;

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

    public <E extends ConfigTemplate> List<E> load(List<InputStream> inputStreams, TemplateProvider<E> provider) throws ConfigException {
        AbstractPool pool = new AbstractPool();
        List<Configuration> configurations = new ArrayList<>();

        for(InputStream stream : inputStreams) {
            configurations.add(new Configuration(stream));
        }

        for(Configuration cfg : configurations) {
            Prototype p = new Prototype(cfg);
            pool.add(p);
        }
        pool.loadAll();

        int i = 0;
        for(Prototype p : pool.getPrototypes()) { // Build all and store root configs.
            p.build(pool, i++);
        }

        List<E> fnlList = new ArrayList<>();

        for(Prototype p : pool.getPrototypes()) {
            AbstractValueProvider valueProvider = new AbstractValueProvider();
            Prototype current = p;
            while(!current.isRoot()) {
                valueProvider.add(current);
                current = p.getParent();
            }
            E template = provider.getInstance();
            delegate.load(template, p.getConfig(), valueProvider);
            fnlList.add(template);
        }

        return fnlList;
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