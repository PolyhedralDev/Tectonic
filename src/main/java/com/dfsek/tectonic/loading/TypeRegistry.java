package com.dfsek.tectonic.loading;

import java.lang.reflect.Type;

/**
 * Implemented by classes that hold a registry of types, mapped to type adapters.
 *
 * @see ConfigLoader
 * @see com.dfsek.tectonic.abstraction.AbstractConfigLoader
 */
public interface TypeRegistry {
    TypeRegistry registerLoader(Type t, TypeLoader<?> loader);
}
