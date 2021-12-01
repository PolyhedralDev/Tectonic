package com.dfsek.tectonic.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Configuration {
    @Nullable
    Object get(@NotNull String key);

    boolean contains(@NotNull String key);

    @Nullable
    String getName();
}
