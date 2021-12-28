package com.dfsek.tectonic.toml;

import com.dfsek.tectonic.api.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

public class TomlConfiguration implements Configuration {
    private final TomlParseResult config;

    private String name;

    public TomlConfiguration(InputStream is) {
        this(is, null);
    }

    public TomlConfiguration(InputStream is, String name) {
        this.name = name;
        try {
            this.config = Toml.parse(is);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public TomlConfiguration(String toml) {
        this(toml, null);
    }

    public TomlConfiguration(String toml, String name) {
        this.name = name;
        config = Toml.parse(toml);
    }

    @Override
    public Object get(@NotNull String key) {
        return deepToObject(config.get(key));
    }

    @SuppressWarnings("unchecked")
    private Object deepToObject(Object result) {
        if(result instanceof TomlArray) return deepToObject(((TomlArray) result).toList());
        if(result instanceof TomlTable) return deepToObject(((TomlTable) result).toMap());
        if(result instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) result;
            map.forEach((key, value) -> map.put(key, deepToObject(value))); // Wont throw CME because we're never adding or removing entries, only updating existing ones.
        } else if(result instanceof List) {
            List<Object> list = (List<Object>) result;
            for(int i = 0; i < list.size(); i++) {
                list.set(i, deepToObject(list.get(i)));
            }
        }
        return result;
    }

    @Override
    public boolean contains(@NotNull String key) {
        return config.contains(key);
    }

    @Override
    public String getName() {
        return name;
    }
}

