package com.dfsek.tectonic.impl.loading.template;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.depth.DepthTracker;
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
    public <T extends ConfigTemplate> T load(T config, Configuration configuration, ValueLoader loader, DepthTracker depthTracker) {
        for(Field field : ReflectionUtil.getFields(config.getClass())) {
            if(!field.isAnnotationPresent(Value.class)) continue;
            Value value = field.getAnnotation(Value.class);

            DepthTracker valueTracker = depthTracker.entry(value.value());

            int m = field.getModifiers();
            if(Modifier.isFinal(m) || Modifier.isStatic(m)) {
                throw new InvalidTemplateException("Field annotated @Value cannot be static or final: " + field.getName() + " of " + config.getClass().getCanonicalName(), depthTracker);
            }

            field.setAccessible(true); // Make field accessible so we can mess with it.

            boolean isFinal = field.isAnnotationPresent(Final.class);
            boolean isDefault = field.isAnnotationPresent(Default.class);

            AnnotatedType type = field.getAnnotatedType();

            try {
                ReflectionUtil.setField(field, config, ReflectionUtil.cast(field.getType(), loader.load(value.value(), type, configuration, valueTracker, isFinal)));
            } catch(ValueMissingException e) {
                if(!isDefault) { // if it's default, we don't care.
                    throw e; // rethrow if it's not default
                }
            } catch(LoadException e) {
                throw e;
            } catch(Exception e) {
                throw new LoadException("Failed to load value", e, depthTracker);
            }
        }
        return config;
    }


}
