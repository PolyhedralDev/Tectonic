package com.dfsek.tectonic.api;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * Implemented by classes that hold a registry of types, mapped to type adapters.
 *
 * @see ConfigLoader
 * @see AbstractConfigLoader
 */
public interface TypeRegistry {
    @NotNull
    @Contract("_, _ -> this")
    TypeRegistry registerLoader(@NotNull Type t, @NotNull TypeLoader<?> loader);

    @NotNull
    @Contract("_, _ -> this")
    <T> TypeRegistry registerLoader(@NotNull Type t, @NotNull Supplier<ObjectTemplate<T>> provider);
}
