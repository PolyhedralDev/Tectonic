package com.dfsek.tectonic.json;

import com.dfsek.tectonic.api.config.Configuration;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class JsonConfiguration implements Configuration {
    private final Object config;

    private final String name;

    public JsonConfiguration(InputStream is) {
        this(is, null);
    }

    public JsonConfiguration(InputStream is, String name) {
        this.name = name;
        this.config = new Gson().fromJson(new InputStreamReader(is), Object.class);
    }

    public JsonConfiguration(String json) {
        this(json, null);
    }

    public JsonConfiguration(String json, String name) {
        this.name = name;
        this.config = new Gson().fromJson(json, Object.class);
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