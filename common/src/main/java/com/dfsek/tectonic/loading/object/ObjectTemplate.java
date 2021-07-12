package com.dfsek.tectonic.loading.object;

import com.dfsek.tectonic.config.ConfigTemplate;

/**
 * ConfigTemplate implementation representing an object, intended to be used in config loading.
 *
 * @param <T> Object type
 */
public interface ObjectTemplate<T> extends ConfigTemplate {
    T get();
}
