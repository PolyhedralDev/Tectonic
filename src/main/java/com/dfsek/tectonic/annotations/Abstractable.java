package com.dfsek.tectonic.annotations;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.exception.ValueMissingException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specified that a config value is abstractable.
 * <p>
 * When a {@link Value} is marked as abstractable, an {@link AbstractConfigLoader} will
 * search parent configurations for the value, before using the default value/throwing a
 * {@link ValueMissingException}.
 *
 * @see AbstractConfigLoader
 * @see Value
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Abstractable {
}
