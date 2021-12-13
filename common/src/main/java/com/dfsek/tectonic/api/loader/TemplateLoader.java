package com.dfsek.tectonic.api.loader;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;

public interface TemplateLoader {
    <T extends ConfigTemplate> T load(T config, Configuration configuration, ValueLoader loader);
}
