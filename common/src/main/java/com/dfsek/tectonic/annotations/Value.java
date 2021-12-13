package com.dfsek.tectonic.annotations;

import com.dfsek.tectonic.impl.loading.ConfigLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a field is a config value.
 * <p>
 * Annotated fields will have values loaded to them when passes through a {@link ConfigLoader}.
 * The value will be retrieved from the location specified by {@link #value()}.
 *
 * @see ConfigLoader
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Value {
    /**
     * The location in the config to load the value from.
     */
    String value();
}
