package com.dfsek.tectonic.json;

import com.dfsek.tectonic.config.Configuration;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class JsonConfiguration  implements Configuration {
    private final Object config;

    private final String name;

    public JsonConfiguration(InputStream is) {
        this(is, is.toString());
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
    public Object get(String key) {
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
    public boolean contains(String key) {
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