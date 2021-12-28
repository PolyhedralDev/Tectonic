package com.dfsek.tectonic.impl.loading.loaders.other;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.time.Duration;

public class DurationLoader implements TypeLoader<Duration> {
    @Override
    public Duration load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) {
        return Duration.parse((String) c);
    }
}
