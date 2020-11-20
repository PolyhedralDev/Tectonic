package com.dfsek.polyconfig.loading;

import java.lang.reflect.Type;

/**
 * Implemented by classes that hold a registry of types, mapped to type adapters.
 */
public interface TypeRegistry {
    TypeRegistry registerLoader(Type t, TypeLoader<?> loader);
}
