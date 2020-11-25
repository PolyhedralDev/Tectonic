package com.dfsek.tectonic.abstraction;

import com.dfsek.tectonic.abstraction.exception.AbstractionException;

import java.util.ArrayList;
import java.util.List;

public class AbstractValueProvider {
    private final List<Prototype> tree = new ArrayList<>();

    public Object get(String key) throws AbstractionException {
        for(Prototype p : tree) {
            if(p.getConfig().contains(key)) return p.getConfig().get(key);
        }
        return null;
    }

    protected void add(Prototype prototype) {
        tree.add(prototype);
    }
}
