package com.dfsek.tectonic.loading.loaders.other;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.time.Duration;

public class DurationLoader implements TypeLoader<Duration> {
    @Override
    public Duration load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader) {
        return Duration.parse((String) c);
    }
}
