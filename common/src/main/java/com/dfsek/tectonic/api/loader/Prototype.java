package com.dfsek.tectonic.api.loader;

import com.dfsek.tectonic.api.exception.abstraction.AbstractionException;
import com.dfsek.tectonic.api.exception.abstraction.CircularInheritanceException;
import com.dfsek.tectonic.api.exception.abstraction.ParentNotFoundException;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.exception.ValidationException;
import com.dfsek.tectonic.impl.abstraction.AbstractPool;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a partially loaded config. A Prototype config has its ID, Abstract, and Extension keys (required by
 * abstraction) loaded, and nothing more.
 */
@SuppressWarnings("unused")
public abstract class Prototype implements ValidatedConfigTemplate {
    private final List<Prototype> children = new ArrayList<>();
    private final Configuration config;
    private final List<Prototype> parents = new ArrayList<>();
    private boolean isRoot = false;


    /**
     * Instantiate a Prototype with a configuration. This will load the prototype using a freshly constructed
     * {@link ConfigLoader}. There are no custom data types in this class, so using a new ConfigLoader prevents
     * pollution.
     *
     * @param config Config to load Prototype from
     * @throws ConfigException If the config contains invalid data.
     */
    public Prototype(Configuration config) throws ConfigException {
        this.config = config;
        new ConfigLoader().load(this, config);
    }

    /**
     * Returns the {@link Configuration} assigned to this Prototype.
     *
     * @return Configuration.
     */
    @NotNull
    public Configuration getConfig() {
        return config;
    }

    /**
     * Returns whether this Prototype is abstract.
     *
     * @return True if this Prototype is abstract.
     */
    public abstract boolean isAbstract();

    /**
     * Build this Prototype's inheritance from an AbstractPool
     *
     * @param pool    AbstractPool to search for parent configs.
     * @param parents Set of parents, to check for circular inheritance.
     * @throws AbstractionException if invalid abstraction data is found.
     */
    @ApiStatus.Internal
    public void build(AbstractPool pool, Set<Prototype> parents) throws AbstractionException {
        String id = getID();
        List<String> extend = getExtends();

        if(parents.contains(this))
            throw new CircularInheritanceException("Circular inheritance detected in config: \"" + id + "\", extending \"" + extend + "\"");

        Set<Prototype> newParents = new HashSet<>(parents);
        newParents.add(this);
        int index = 0;
        for(String parentID : extend) {
            Prototype parent = pool.get(parentID);
            if(parent == null)
                throw new ParentNotFoundException("No such config \"" + parentID + "\". Specified as parent of \"" + id + "\" at index " + index);
            this.parents.add(parent);
            parent.build(pool, newParents); // Build the parent, to recursively build the entire tree.
            index++;
        }
        if(extend.size() == 0) {
            isRoot = true;
        }
    }

    /**
     * Gets the ID of this Prototype.
     *
     * @return ID of Prototype.
     */
    @NotNull
    public abstract String getID();

    @NotNull
    public abstract List<String> getExtends();

    /**
     * Get this Prototype's parent, if it exists.
     *
     * @return This Prototype's parent. {@code null} if the parent does not exist, or has not been loaded.
     * @see #build
     */
    @NotNull
    public List<Prototype> getParents() {
        return parents;
    }

    /**
     * Returns whether this Prototype is a root (Has no parents)
     *
     * @return Whether this is a root in the inheritance tree.
     */
    public boolean isRoot() {
        return isRoot;
    }

    @Override
    public boolean validate() throws ValidationException {
        String id = getID();
        if(!id.matches("^[a-zA-Z0-9_-]*$"))
            throw new ValidationException("ID must only contain alphanumeric characters, hyphens, and underscores. \"" + id + "\" is not a valid ID.");
        return true;
    }
}
