package com.dfsek.tectonic.api.config.template.annotations;

import com.dfsek.tectonic.api.loader.AbstractConfigLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a config value is <b>not</b> abstractable.
 * <p>
 * When a {@link Value} is marked as final, an {@link AbstractConfigLoader} will not
 * search parent configurations for the value, instead using the value in the base config.
 *
 * @see AbstractConfigLoader
 * @see Value
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Final {
}
