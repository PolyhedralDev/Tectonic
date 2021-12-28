package com.dfsek.tectonic.yaml;

import com.dfsek.tectonic.api.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * Object representation of a YAML configuration.
 */
public class YamlConfiguration implements Configuration {
    private final Object config;

    private final String name;

    public YamlConfiguration(InputStream is) {
        this(is, null);
    }

    public YamlConfiguration(InputStream is, String name) {
        this.name = name;
        config = new Yaml().load(is);
    }

    public YamlConfiguration(String yaml) {
        this(yaml, null);
    }

    public YamlConfiguration(String yaml, String name) {
        this.name = name;
        config = new Yaml().load(yaml);
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
