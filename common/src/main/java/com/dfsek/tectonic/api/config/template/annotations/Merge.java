package com.dfsek.tectonic.api.config.template.annotations;

import com.dfsek.tectonic.api.loader.AbstractConfigLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Specifies that an inherited {@link java.util.Map} or {@link java.util.List} value is merged across the inheritance
 * tree, instead of being taken whole from the nearest config that defines it.
 * <p>
 * When a config loaded by an {@link AbstractConfigLoader} has ancestors, the value of every config in the tree that
 * defines the key is loaded on its own, with the normal type loading (so type preprocessors run once per config), and
 * the results are combined from the farthest ancestor to the base config: maps with
 * {@link java.util.Map#putAll(java.util.Map)}, lists with {@link java.util.List#addAll(java.util.Collection)}. Nearer
 * configs win per map key, and their list elements come later; among configs at the same depth, the first one in
 * {@code extends} order counts as nearest, the same config
 * {@link com.dfsek.tectonic.impl.abstraction.AbstractConfiguration#get(String)} would pick.
 * <p>
 * The result is a new instance of the field's declared class, or a {@link java.util.LinkedHashMap} or
 * {@link java.util.ArrayList} when the declared type is an interface or abstract class.
 * <p>
 * On a config without inheritance this behaves like an ordinary {@link Value}. If no config defines the key, the
 * usual {@link Default} or missing-value behaviour applies.
 * <p>
 * The field type must be a {@link java.util.Map} or {@link java.util.List}, and the field must not be {@link Final}: a final value is not
 * inherited, so there is nothing to merge. Either is reported as an
 * {@link com.dfsek.tectonic.api.exception.InvalidTemplateException} when the template is loaded.
 *
 * @see Value
 * @see AbstractConfigLoader
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Merge {
}
