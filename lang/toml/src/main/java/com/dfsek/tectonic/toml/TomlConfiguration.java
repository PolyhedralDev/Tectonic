package com.dfsek.tectonic.toml;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigParseException;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TomlConfiguration  implements Configuration {
    private final TomlParseResult config;

    private String name;

    public TomlConfiguration(InputStream is) {
        this(is, is.toString());
    }

    public TomlConfiguration(InputStream is, String name) {
        this.name = name;
        try {
            this.config = Toml.parse(is);
        } catch(IOException e) {
            throw new ConfigParseException("Failed to parse TOML: ", e);
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
    public Object get(String key) {
        Object result = config.get(key);
        if(result instanceof TomlArray) return ((TomlArray) result).toList();
        if(result instanceof TomlTable) return  ((TomlTable) result).toMap();
        return result;
    }

    @Override
    public boolean contains(String key) {
        return config.contains(key);
    }

    @Override
    public String getName() {
        return name;
    }
}

