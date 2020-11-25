package com.dfsek.tectonic.abstraction;

import com.dfsek.tectonic.ConfigTemplate;

public interface TemplateProvider<E extends ConfigTemplate> {
    E getInstance();
}
