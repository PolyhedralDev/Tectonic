package com.dfsek.tectonic.abstraction;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.TypeRegistry;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to load several configs that may depend on each other.
 */
public class AbstractConfigLoader implements TypeRegistry {
    private final ConfigLoader delegate = new ConfigLoader();

    @Override
    public TypeRegistry registerLoader(Type t, TypeLoader<?> loader) {
        delegate.registerLoader(t, loader);
        return this;
    }

    /**
     * @param inputStreams List of InputStreams to load configs from.
     * @param provider     TemplateProvide to get ConfigTemplate instances from.
     * @param <E>          ConfigTemplate type.
     * @return List of loaded ConfigTemplates.
     * @throws ConfigException If configs contain errors.
     */
    public <E extends ConfigTemplate> List<E> load(List<InputStream> inputStreams, TemplateProvider<E> provider) throws ConfigException {
        AbstractPool pool = new AbstractPool();
        for(InputStream stream : inputStreams) {
            Prototype p = new Prototype(new Configuration(stream));
            pool.add(p);
        }
        pool.loadAll();

        List<E> fnlList = new ArrayList<>();

        for(Prototype p : pool.getPrototypes()) {
            if(p.isAbstract())
                continue; // Don't directly load abstract configs. They will be loaded indirectly via inheritance tree building.
            System.out.println("Loading " + p.getId());
            AbstractValueProvider valueProvider = new AbstractValueProvider();
            Prototype current = p;
            while(current != null && !current.isRoot()) {
                valueProvider.add(current.getParent());
                current = current.getParent();
            }
            E template = provider.getInstance();
            delegate.load(template, p.getConfig(), valueProvider);
            fnlList.add(template);
        }
        return fnlList;
    }
}