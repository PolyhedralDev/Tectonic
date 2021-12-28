package com.dfsek.tectonic.api.depth;

class IntrinsicLevel implements Level{
    private final String verbose;

    public IntrinsicLevel(String verbose) {
        this.verbose = verbose;
    }

    @Override
    public String descriptor() {
        return "";
    }

    @Override
    public String joinDescriptor() {
        return "";
    }

    @Override
    public String verboseDescriptor() {
        return verbose;
    }
}
