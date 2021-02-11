package com.dfsek.tectonic.abstraction;

import com.dfsek.tectonic.loading.ConfigLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to provide abstracted values to a {@link ConfigLoader}.
 * <p>
 * This class holds an inheritance tree of {@link Prototype}s, and gets values from them, for loading.
 */
public class AbstractValueProvider {
    private final List<Prototype> tree = new ArrayList<>();

    /**
     * Get a value from its lowest point in the inheritance tree.
     *
     * @param key Key to get from the tree.
     * @return Object loaded from the key. (Raw config object, not type adapted!)
     */
    public Object get(String key) {
        for(Prototype p : tree) {
            if(p.getConfig().contains(key)) return p.getConfig().get(key);
        }
        return null;
    }

    /**
     * Get a value in all configs it is defined in.
     * @param key Key to search for
     * @return List containing all results.
     */
    public List<Object> getAll(String key) {
        List<Object> results = new ArrayList<>();
        for(Prototype p : tree) {
            if(p.getConfig().contains(key)) results.add(p.getConfig().get(key));
        }
        return results;
    }

    /**
     * Add a prototype to the top of the inheritance tree.
     *
     * @param prototype Prototype to add
     */
    protected void add(Prototype prototype) {
        tree.add(prototype);
    }
}
