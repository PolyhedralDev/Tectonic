package com.dfsek.tectonic.config;

public interface Configuration {
    @SuppressWarnings("unchecked")
    Object get(String key);

    @SuppressWarnings("unchecked")
    boolean contains(String key);

    String getName();
}
