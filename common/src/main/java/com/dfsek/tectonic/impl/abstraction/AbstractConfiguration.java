package com.dfsek.tectonic.impl.abstraction;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;


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
     *
     * @return Object loaded from the key. (Raw config object, not type adapted!)
     */
    public Object get(@NotNull String key) {
        for(Layer p : tree) {
            Object l = p.get(key);
            if(l != null) return l;
        }
        return null;
    }

    /**
     * Get every value for a key across the inheritance tree, for merging.
     * <p>
     * Values are ordered from lowest to highest priority: the reverse of the order in which {@link #get(String)}
     * searches. That is farthest ancestor layer first and the base config last; within one layer, later prototypes in
     * {@code extends} order come before earlier ones, so that the first prototype (the one {@link #get(String)} would
     * pick) comes last. Applying the values in order with {@link java.util.Map#putAll(java.util.Map)} therefore lets
     * nearer configs win.
     * <p>
     * Configs that do not define the key, or define it as {@code null}, are skipped. A prototype reachable through
     * several paths (diamond inheritance) contributes once, at its highest-priority position.
     *
     * @param key Key to get from the tree.
     *
     * @return Raw config objects (not type adapted!), lowest priority first. Empty if no config defines the key.
     */
    public List<Object> getAll(@NotNull String key) {
        List<Object> values = new ArrayList<>();
        Set<Prototype> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for(Layer layer : tree) { // Highest priority first, as in get().
            for(Prototype prototype : layer.items) {
                if(!seen.add(prototype)) continue;
                Configuration config = prototype.getConfig();
                if(!config.contains(key)) continue;
                Object value = config.get(key);
                if(value != null) values.add(value);
            }
        }
        Collections.reverse(values);
        return values;
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
