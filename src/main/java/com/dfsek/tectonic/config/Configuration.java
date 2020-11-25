package com.dfsek.tectonic.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class Configuration {
    private final Map<String, Object> config;

    public Configuration(InputStream is) {
        config = new Yaml().load(is);
    }

    public Configuration(String yaml) {
        config = new Yaml().load(yaml);
    }

    public Configuration(Map<String, Object> map) {
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
}
