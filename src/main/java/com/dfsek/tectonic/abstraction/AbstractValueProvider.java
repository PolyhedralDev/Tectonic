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
    private final List<Layer> tree = new ArrayList<>();
    private int layer = 0;

    public AbstractValueProvider() {
        tree.add(new Layer());
    }

    /**
     * Get a value from its lowest point in the inheritance tree.
     *
     * @param key Key to get from the tree.
     * @return Object loaded from the key. (Raw config object, not type adapted!)
     */
    public Object get(String key) {
        for(Layer p : tree) {
            Object l = p.get(key);
            if(l != null) return l;
        }
        return null;
    }

    /**
     * Add a prototype to the top of the inheritance tree.
     *
     * @param prototype Prototype to add
     */
    protected void add(Prototype prototype) {
        tree.get(layer).add(prototype);
    }

    protected int next() {
        int l0 = layer;
        layer++;
        tree.add(new Layer());
        return l0;
    }

    protected void reset(int layer) {
        this.layer = layer;
    }

    private static final class Layer {
        private final List<Prototype> items = new ArrayList<>();

        public void add(Prototype item) {
            items.add(item);
        }

        public Object get(String key) {
            for(Prototype p : items) {
                if(p.getConfig().contains(key)) return p.getConfig().get(key);
            }
            return null;
        }
    }
}
