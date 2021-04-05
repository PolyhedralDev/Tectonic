package com.dfsek.tectonic.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * Object representation of a YAML configuration.
 */
public class Configuration {
    private final Map<String, Object> config;

    private String name;

    public Configuration(InputStream is) {
        this.name = is.toString();
        config = new Yaml().load(is);
    }

    public Configuration(InputStream is, String name) {
        this.name = name;
        config = new Yaml().load(is);
    }

    public Configuration(String yaml) {
        config = new Yaml().load(yaml);
    }

    public Configuration(String yaml, String name) {
        this.name = name;
        config = new Yaml().load(yaml);
    }

    public Configuration(Map<String, Object> map) {
        this.config = map;
    }

    public Configuration(Map<String, Object> map, String name) {
        this.name = name;
        this.config = map;
    }

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

    public String getName() {
        return name;
    }
}
