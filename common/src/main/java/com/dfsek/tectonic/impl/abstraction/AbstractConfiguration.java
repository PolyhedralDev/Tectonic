package com.dfsek.tectonic.impl.abstraction;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to provide abstracted values to a {@link ConfigLoader}.
 * <p>
 * This class holds an inheritance tree of {@link Prototype}s, and gets values from them, for loading.
 */
public class AbstractConfiguration implements Configuration {
    private final List<Layer> tree = new ArrayList<>();
    private final Prototype base;
    private int layer = 0;

    public AbstractConfiguration(Prototype base) {
        this.base = base;
        tree.add(new Layer());
    }

    /**
     * Get a value from its lowest point in the inheritance tree.
     *
     * @param key Key to get from the tree.
     * @return Object loaded from the key. (Raw config object, not type adapted!)
     */
    public Object get(@NotNull String key) {
        for(Layer p : tree) {
            Object l = p.get(key);
            if(l != null) return l;
        }
        return null;
    }

    public Object getBase(String key) {
        return base.getConfig().get(key);
    }

    public String getID() {
        return base.getID();
    }

    public Prototype getBase() {
        return base;
    }

    @Override
    public boolean contains(@NotNull String key) {
        for(Layer p : tree) {
            if(p.contains(key)) return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return getID();
    }

    /**
     * Add a prototype to the top of the inheritance tree.
     *
     * @param prototype Prototype to add
     */
    public void add(Prototype prototype) {
        tree.get(layer).add(prototype);
    }

    public int next() {
        int l0 = layer;
        layer++;
        tree.add(new Layer());
        return l0;
    }

    public void reset(int layer) {
        this.layer = layer;
    }

    public boolean containsBase(String key) {
        return base.getConfig().contains(key);
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

        public boolean contains(String key) {
            for(Prototype p : items) {
                if(p.getConfig().contains(key)) return true;
            }
            return false;
        }
    }
}
