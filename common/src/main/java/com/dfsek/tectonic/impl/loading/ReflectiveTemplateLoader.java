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
import com.dfsek.tectonic.api.loader.ValueLoader;
import com.dfsek.tectonic.util.ReflectionUtil;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectiveTemplateLoader implements TemplateLoader {
    @Override
    public <T extends ConfigTemplate> T load(T config, Configuration configuration, ValueLoader loader) {
        for(Field field : ReflectionUtil.getFields(config.getClass())) {
            if(!field.isAnnotationPresent(Value.class)) continue;
            Value value = field.getAnnotation(Value.class);

            int m = field.getModifiers();
            if(Modifier.isFinal(m) || Modifier.isStatic(m)) {
                throw new InvalidTemplateException("Field annotated @Value cannot be static or final: " + field.getName() + " of " + config.getClass().getCanonicalName());
            }

            field.setAccessible(true); // Make field accessible so we can mess with it.

            boolean isFinal = field.isAnnotationPresent(Final.class);
            boolean isDefault = field.isAnnotationPresent(Default.class);

            AnnotatedType type = field.getAnnotatedType();

            try {
                ReflectionUtil.setField(field, config, ReflectionUtil.cast(field.getType(), loader.load(value.value(), type, configuration, isFinal)));
            } catch(ValueMissingException e) {
                if(!isDefault) { // if it's default, we don't care.
                    throw e; // rethrow if it's not default
                }
            } catch(Exception e) {
                throw new LoadException("Failed to load value \"" + value.value() + "\" to field \"" + field.getName() + "\" in config \"" + configuration.getName() + "\": " + e.getMessage(), e);
            }
        }
        return config;
    }


}
