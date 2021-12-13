package com.dfsek.tectonic.impl.loading;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.InvalidTemplateException;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.exception.ValueMissingException;
import com.dfsek.tectonic.api.loader.TemplateLoader;
import com.dfsek.tectonic.impl.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.util.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

public class ReflectiveTemplateLoader implements TemplateLoader {
    @Override
    public <T extends ConfigTemplate> T load(T config, Configuration configuration, BiFunction<AnnotatedType, Object, Object> loader) {
        for(Field field : ReflectionUtil.getFields(config.getClass())) {
            if(!field.isAnnotationPresent(Value.class)) continue;
            Value value = field.getAnnotation(Value.class);

            int m = field.getModifiers();
            if(Modifier.isFinal(m) || Modifier.isStatic(m)) {
                throw new InvalidTemplateException("Field annotated @Value cannot be static or final: " + field.getName() + " of " + config.getClass().getCanonicalName());
            }

            field.setAccessible(true); // Make field accessible so we can mess with it.

            boolean isFinal = field.isAnnotationPresent(Final.class);
            boolean defaultable = field.isAnnotationPresent(Default.class);

            AnnotatedType type = field.getAnnotatedType();

            try {
                if(containsFinal(configuration, value.value())) { // If config contains value, load it.
                    Object loadedObject = loader.apply(type, getFinal(configuration, value.value())); // Re-assign if type is found in registry.
                    ReflectionUtil.setField(field, config, ReflectionUtil.cast(field.getType(), loadedObject)); // Set the field to the loaded value.
                } else if(!isFinal && (configuration instanceof AbstractConfiguration)) { // If value is abstractable, try to get it from parent configs.
                    Object abs = configuration.get(value.value());
                    if(abs == null) {
                        if(defaultable) continue;
                        throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config, or its parents: " + configuration.getName()); // Throw exception if value is not provided, and isn't in parents.
                    }
                    abs = loader.apply(type, abs);
                    ReflectionUtil.setField(field, config, ReflectionUtil.cast(field.getType(), abs));
                } else if(!defaultable) {
                    throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config: " + configuration.getName()); // Throw exception if value is not provided, and isn't abstractable
                }
            } catch(Exception e) {
                throw new LoadException("Failed to load value \"" + value.value() + "\" to field \"" + field.getName() + "\" in config \"" + configuration.getName() + "\": " + e.getMessage(), e);
            }
        }
        return config;
    }

    private Object getFinal(Configuration configuration, String key) {
        if(configuration instanceof AbstractConfiguration) return ((AbstractConfiguration) configuration).getBase(key);
        return configuration.get(key);
    }

    private boolean containsFinal(Configuration configuration, String key) {
        if(configuration instanceof AbstractConfiguration)
            return ((AbstractConfiguration) configuration).containsBase(key);
        return configuration.contains(key);
    }
}
