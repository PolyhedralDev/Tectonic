package com.dfsek.tectonic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that this value is able to be merged with parent values in abstract configs.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Merge {
    Type value();
    enum Type {
        /**
         * Merge only if a condition is met.
         */
        CONDITIONAL,
        /**
         * Always merge.
         */
        ALWAYS
    }
}
