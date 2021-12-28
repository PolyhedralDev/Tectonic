package com.dfsek.tectonic.api.depth;

/**
 * An entry in a map.
 */
public class EntryLevel implements Level {
    private final String name;

    public EntryLevel(String name) {
        this.name = name;
    }

    @Override
    public String identify() {
        return name;
    }

    @Override
    public String joinDescriptor() {
        return ".";
    }
}
