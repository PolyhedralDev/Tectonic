package com.dfsek.tectonic.impl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the annotated element is not to be written by the programmer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Generated {
}
