package com.dfsek.tectonic.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * Object representation of a YAML configuration.
 */
public class YamlConfiguration implements Configuration {
    private final Map<String, Object> config;

    private String name;

    public YamlConfiguration(InputStream is) {
        this.name = is.toString();
        config = new Yaml().load(is);
    }

    public YamlConfiguration(InputStream is, String name) {
        this.name = name;
        config = new Yaml().load(is);
    }

    public YamlConfiguration(String yaml) {
        config = new Yaml().load(yaml);
    }

    public YamlConfiguration(String yaml, String name) {
        this.name = name;
        config = new Yaml().load(yaml);
    }

    public YamlConfiguration(Map<String, Object> map) {
        this.config = map;
    }

    public YamlConfiguration(Map<String, Object> map, String name) {
        this.name = name;
        this.config = map;
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
