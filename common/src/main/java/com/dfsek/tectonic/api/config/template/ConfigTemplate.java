package com.dfsek.tectonic.api.config.template;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.impl.loading.ConfigLoader;

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
}
