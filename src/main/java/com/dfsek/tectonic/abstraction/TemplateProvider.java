package com.dfsek.tectonic.abstraction;

import com.dfsek.tectonic.ConfigTemplate;

/**
 * Functional interface to provide instances of {@link ConfigTemplate} to use in {@link AbstractConfigLoader}.
 *
 * @param <E> Type of ConfigTemplate to provide.
 */
@FunctionalInterface
public interface TemplateProvider<E extends ConfigTemplate> {
    /**
     * Get an instance of the {@link ConfigTemplate} specified.
     *
     * @return ConfigTemplate instance.
     */
    E getInstance();
}
