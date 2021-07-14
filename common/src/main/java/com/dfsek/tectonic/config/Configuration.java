package com.dfsek.tectonic.config;

public interface Configuration {
    Object get(String key);

    boolean contains(String key);

    String getName();
}
