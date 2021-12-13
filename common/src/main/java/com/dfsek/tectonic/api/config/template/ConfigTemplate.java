package com.dfsek.tectonic.api.config.template;

import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.TemplateLoader;
import com.dfsek.tectonic.impl.loading.template.ReflectiveTemplateLoader;

/**
 * Interface to be implemented by classes containing annotated fields to be loaded by
 * a {@link ConfigLoader}.
 *
 * @see Value
 * @see ConfigLoader
 * @see AbstractConfigLoader
 * @see ValidatedConfigTemplate
 */
public interface ConfigTemplate {
    default TemplateLoader loader() {
        return new ReflectiveTemplateLoader();
    }
}
