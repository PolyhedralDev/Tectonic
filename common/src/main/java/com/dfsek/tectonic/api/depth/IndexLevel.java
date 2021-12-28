package com.dfsek.tectonic.api.depth;

public class IndexLevel implements Level {
    private final int index;

    public IndexLevel(int index) {
        this.index = index;
    }

    @Override
    public String descriptor() {
        return "[" + index + "]";
    }

    @Override
    public String joinDescriptor() {
        return "";
    }

    @Override
    public String verboseDescriptor() {
        return "At index " + index;
    }

    public int getIndex() {
        return index;
    }
}
