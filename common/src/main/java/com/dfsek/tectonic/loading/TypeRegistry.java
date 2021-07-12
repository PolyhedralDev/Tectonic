package com.dfsek.tectonic.loading;

import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * Implemented by classes that hold a registry of types, mapped to type adapters.
 *
 * @see ConfigLoader
 * @see com.dfsek.tectonic.abstraction.AbstractConfigLoader
 */
public interface TypeRegistry {
    TypeRegistry registerLoader(Type t, TypeLoader<?> loader);

    <T> TypeRegistry registerLoader(Type t, Supplier<ObjectTemplate<T>> provider);
}
