package com.dfsek.tectonic.api.config.template.dynamic;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.exception.ValueMissingException;
import com.dfsek.tectonic.api.loader.TemplateLoader;
import com.dfsek.tectonic.api.loader.ValueLoader;
import com.dfsek.tectonic.util.ClassAnnotatedTypeImpl;

import java.util.Map;

public class DynamicTemplateLoader implements TemplateLoader {
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ConfigTemplate> T load(T config, Configuration configuration, ValueLoader loader) {
        if(!(config instanceof DynamicTemplate)) {
            throw new IllegalArgumentException("Must receive DynamicTemplate, got " + config.getClass().getCanonicalName());
        }

        DynamicTemplate dynamicTemplate = (DynamicTemplate) config;
        Map<String, Object> computed = (Map<String, Object>) dynamicTemplate.getComputed();

        dynamicTemplate.getValues().forEach((id, value) -> {
            try {
                computed.put(id, loader.load(id, new ClassAnnotatedTypeImpl(value.getType()), configuration, value.isFinal()));
            } catch(ValueMissingException e) {
                if(value.isDefault()) {
                    computed.put(id, value.getDefaultValue());
                } else throw e;
            }
        });

        return config;
    }
}
