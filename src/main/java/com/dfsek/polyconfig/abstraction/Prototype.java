package com.dfsek.polyconfig.abstraction;

import com.dfsek.polyconfig.ConfigTemplate;
import com.dfsek.polyconfig.abstraction.exception.AbstractionException;
import com.dfsek.polyconfig.abstraction.exception.CircularInheritanceException;
import com.dfsek.polyconfig.abstraction.exception.ParentNotFoundException;
import com.dfsek.polyconfig.annotations.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a partially loaded config. A Prototype config has its ID and Extension keys (required by abstraction)
 * loaded, and nothing more.
 */
@SuppressWarnings("unused")
public class Prototype implements ConfigTemplate {
    private final List<Prototype> children = new ArrayList<>();
    private final List<Integer> UIDs = new ArrayList<>();
    private Prototype parent;
    private boolean isRoot = false;
    @Value("id")
    private String id;
    @Value("extends")
    private String extend;

    /**
     * Build this Prototype's inheritance from an AbstractPool
     *
     * @param pool     AbstractPool to search for parent configs.
     * @param chainUID Unique identifier for this inheritance tree. Used to check for circular inheritance.
     */
    protected void build(AbstractPool pool, int chainUID) throws AbstractionException {
        if(UIDs.contains(chainUID))
            throw new CircularInheritanceException("Circular inheritance detected in config: \"" + getId() + "\", extending \"" + getExtend() + "\"");
        UIDs.add(chainUID);
        if(extend != null) {
            Prototype p = pool.get(extend);
            if(p == null)
                throw new ParentNotFoundException("No such config \"" + extend + "\". Specified as parent of \"" + id + "\"");
            this.parent = p;
            parent.build(pool, chainUID); // Build the parent, to recursively build the entire tree.
        } else isRoot = true;
    }

    public String getId() {
        return id;
    }

    public String getExtend() {
        return extend;
    }

    public Prototype getParent() {
        return parent;
    }

    public boolean isRoot() {
        return isRoot;
    }
}
