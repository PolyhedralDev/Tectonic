package com.dfsek.tectonic;

import com.dfsek.tectonic.api.config.Configuration;
import com.typesafe.config.ConfigFactory;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HoconConfiguration implements Configuration {
    private final Object config;

    private final String name;

    public HoconConfiguration(InputStream is) {
        this(is, null);
    }

    public HoconConfiguration(InputStream is, String name) {
        this.name = name;
        this.config = ConfigFactory.parseReader(new InputStreamReader(is)).root().unwrapped();
    }

    public HoconConfiguration(String hocon) {
        this(hocon, null);
    }

    public HoconConfiguration(String hocon, String name) {
        this.name = name;
        this.config = ConfigFactory.parseString(hocon).root().unwrapped();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object get(@NotNull String key) {
        String[] levels = key.split("\\.");
        Object level = config;
        for(String keyLevel : levels) {
            if(!(level instanceof Map)) throw new IllegalArgumentException();
            level = ((Map<String, Object>) level).get(keyLevel);
        }
        return level;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(@NotNull String key) {
        String[] levels = key.split("\\.");
        Object level = config;
        for(String keyLevel : levels) {
            if(!(level instanceof Map)) return false;
            level = ((Map<String, Object>) level).get(keyLevel);
        }
        return !(level == null);
    }

    @Override
    public String getName() {
        return name;
    }
}
