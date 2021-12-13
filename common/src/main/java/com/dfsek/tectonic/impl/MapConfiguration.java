package com.dfsek.tectonic.impl;

import com.dfsek.tectonic.api.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MapConfiguration implements Configuration {
    private final Map<String, Object> config;

    private String name;

    public MapConfiguration(Map<String, Object> map) {
        this.config = map;
    }

    public MapConfiguration(Map<String, Object> map, String name) {
        this.name = name;
        this.config = map;
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
