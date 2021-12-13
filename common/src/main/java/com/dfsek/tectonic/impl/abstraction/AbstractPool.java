package com.dfsek.tectonic.impl.abstraction;

import com.dfsek.tectonic.api.exception.abstraction.AbstractionException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Pool holding all abstract configs that can be grouped together.
 */
public class AbstractPool {
    private final Map<String, Prototype> pool = new HashMap<>();

    /**
     * Add a {@link Prototype} to the pool.
     *
     * @param prototype Prototype to add.
     */
    public void add(Prototype prototype) {
        pool.put(prototype.getID(), prototype);
    }

    public Prototype get(String id) {
        return pool.get(id);
    }

    /**
     * Build all the {@link Prototype}s in this pool.
     *
     * @throws AbstractionException If there are errors in the configs.
     */
    public void loadAll() throws AbstractionException {
        for(Map.Entry<String, Prototype> entry : pool.entrySet()) {
            entry.getValue().build(this, Collections.emptySet());
        }
    }

    /**
     * Get the Set of Prototypes contained in this pool.
     *
     * @return Set of Prototypes.
     */
    public Set<Prototype> getPrototypes() {
        return new HashSet<>(pool.values());
    }
}
