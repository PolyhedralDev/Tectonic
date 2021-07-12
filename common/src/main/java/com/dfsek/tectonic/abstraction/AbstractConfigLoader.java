package com.dfsek.tectonic.abstraction;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Class to load several configs that may depend on each other.
 */
public class AbstractConfigLoader implements TypeRegistry {
    private final ConfigLoader delegate = new ConfigLoader();

    @Override
    public AbstractConfigLoader registerLoader(Type t, TypeLoader<?> loader) {
        delegate.registerLoader(t, loader);
        return this;
    }

    @Override
    public <T> AbstractConfigLoader registerLoader(Type t, Supplier<ObjectTemplate<T>> provider) {
        delegate.registerLoader(t, provider);
        return this;
    }

    /**
     * @param configurations List of Configurations to load configs from.
     * @param provider       TemplateProvide to get ConfigTemplate instances from.
     * @param <E>            ConfigTemplate type.
     * @return List of loaded ConfigTemplates.
     * @throws ConfigException If configs contain errors.
     */
    public <E extends ConfigTemplate> List<E> loadConfigs(List<Configuration> configurations, Supplier<E> provider) throws ConfigException {
        AbstractPool pool = new AbstractPool();
        for(Configuration config : configurations) {
            Prototype p = new Prototype(config);
            pool.add(p);
        }
        pool.loadAll();

        Map<Prototype, E> fnlList = new HashMap<>();

        for(Prototype p : pool.getPrototypes()) {
            if(p.isAbstract())
                continue; // Don't directly load abstract configs. They will be loaded indirectly via inheritance tree building.
            AbstractConfiguration valueProvider = new AbstractConfiguration();
            build(valueProvider, Collections.singletonList(p));
            E template = provider.get();
            try {
                delegate.load(template, valueProvider);
            } catch(ConfigException e) {
                throw new LoadException("Failed to load config with ID \"" + p.getID() + "\": " + e.getMessage(), e);
            }
            fnlList.put(p, template);
        }

        for(Map.Entry<Prototype, E> entry : fnlList.entrySet()) {
            if(entry.getValue() instanceof ValidatedConfigTemplate && !((ValidatedConfigTemplate) entry.getValue()).validate()) {
                throw new ValidationException("Failed to validate config \"" + entry.getKey().getID() + ". Reason unspecified.");
            }
        }

        return new ArrayList<>(fnlList.values());
    }

    private void build(AbstractConfiguration provider, List<Prototype> prototypes) {
        for(Prototype prototype : prototypes) {
            provider.add(prototype);
            if(!prototype.isRoot()) {
                int layer = provider.next();
                build(provider, prototype.getParents());
                provider.reset(layer);
            }
        }
    }
}