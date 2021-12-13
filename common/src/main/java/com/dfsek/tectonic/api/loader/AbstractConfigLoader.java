package com.dfsek.tectonic.api.loader;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.preprocessor.ValuePreprocessor;
import com.dfsek.tectonic.impl.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.impl.abstraction.AbstractPool;
import com.dfsek.tectonic.impl.abstraction.Prototype;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Class to load several configs that may depend on each other.
 */
public class AbstractConfigLoader implements TypeRegistry {
    private final ConfigLoader delegate = new ConfigLoader();

    @Override
    public @NotNull AbstractConfigLoader registerLoader(@NotNull Type t, @NotNull TypeLoader<?> loader) {
        delegate.registerLoader(t, loader);
        return this;
    }

    @Override
    public <T> @NotNull AbstractConfigLoader registerLoader(@NotNull Type t, @NotNull Supplier<ObjectTemplate<T>> provider) {
        delegate.registerLoader(t, provider);
        return this;
    }

    public <T extends Annotation> AbstractConfigLoader registerPreprocessor(Class<? extends T> clazz, ValuePreprocessor<T> processor) {
        delegate.registerPreprocessor(clazz, processor);
        return this;
    }

    public Set<AbstractConfiguration> loadConfigs(List<Configuration> configurations) throws ConfigException {
        AbstractPool pool = new AbstractPool();
        for(Configuration config : configurations) {
            Prototype p = new Prototype(config);
            pool.add(p);
        }
        pool.loadAll();

        Set<AbstractConfiguration> abstractConfigs = new HashSet<>();
        for(Prototype p : pool.getPrototypes()) {
            if(p.isAbstract())
                continue; // Don't directly load abstract configs. They will be loaded indirectly via inheritance tree building.
            AbstractConfiguration config = new AbstractConfiguration(p);
            build(config, Collections.singletonList(p));
            abstractConfigs.add(config);
        }
        return abstractConfigs;
    }

    /**
     * @param configurations List of Configurations to load configs from.
     * @param provider       TemplateProvide to get ConfigTemplate instances from.
     * @param <E>            ConfigTemplate type.
     * @return List of loaded ConfigTemplates.
     * @throws ConfigException If configs contain errors.
     */
    public <E extends ConfigTemplate> Set<E> loadTemplates(List<Configuration> configurations, Supplier<E> provider) throws ConfigException {
        Set<E> templates = new HashSet<>();
        loadConfigs(configurations).forEach(config -> templates.add(delegate.load(provider.get(), config)));
        return templates;
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