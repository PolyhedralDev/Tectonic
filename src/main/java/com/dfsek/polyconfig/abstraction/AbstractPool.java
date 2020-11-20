package com.dfsek.polyconfig.abstraction;

import com.dfsek.polyconfig.abstraction.exception.AbstractionException;

import java.util.HashMap;
import java.util.Map;

/**
 * Pool holding all abstract configs that can be grouped together.
 */
public class AbstractPool {
    private final Map<String, Prototype> pool = new HashMap<>();

    public void add(Prototype prototype) {
        pool.put(prototype.getId(), prototype);
    }

    public Prototype get(String id) {
        return pool.get(id);
    }

    public void loadAll() throws AbstractionException {
        int i = 0;
        for(Map.Entry<String, Prototype> entry : pool.entrySet()) {
            entry.getValue().build(this, i++);
        }
    }
}
