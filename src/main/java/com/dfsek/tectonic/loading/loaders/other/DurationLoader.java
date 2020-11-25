package com.dfsek.tectonic.loading.loaders.other;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationLoader implements TypeLoader<Duration> {
    @Override
    public Duration load(Type t, Object c, ConfigLoader loader) {
        return Duration.parse((String) c);
    }
}
