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
import org.yaml.snakeyaml.error.YAMLException;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            try {
                Prototype p = new Prototype(new Configuration(stream));
                pool.add(p);
            } catch(YAMLException e) {
                throw new LoadException("Failed to parse YAML: " + e.getMessage(), e);
            }
        }
        pool.loadAll();

        Map<Prototype, E> fnlList = new HashMap<>();

        for(Prototype p : pool.getPrototypes()) {
            if(p.isAbstract())
                continue; // Don't directly load abstract configs. They will be loaded indirectly via inheritance tree building.
            AbstractValueProvider valueProvider = new AbstractValueProvider();
            Prototype current = p;
            while(current != null && !current.isRoot()) {
                valueProvider.add(current.getParent());
                current = current.getParent();
            }
            E template = provider.getInstance();
            try {
                delegate.load(template, p.getConfig(), valueProvider);
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
}