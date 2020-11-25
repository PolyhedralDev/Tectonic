package com.dfsek.tectonic.loading;

import com.dfsek.tectonic.abstraction.AbstractValueProvider;
import com.dfsek.tectonic.abstraction.exception.ProviderMissingException;
import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.exception.ReflectiveAccessException;
import com.dfsek.tectonic.exception.ValueMissingException;
import com.dfsek.tectonic.loading.loaders.StringLoader;
import com.dfsek.tectonic.loading.loaders.generic.ArrayListLoader;
import com.dfsek.tectonic.loading.loaders.generic.HashMapLoader;
import com.dfsek.tectonic.loading.loaders.generic.HashSetLoader;
import com.dfsek.tectonic.loading.loaders.other.DurationLoader;
import com.dfsek.tectonic.loading.loaders.primitives.BooleanLoader;
import com.dfsek.tectonic.loading.loaders.primitives.ByteLoader;
import com.dfsek.tectonic.loading.loaders.primitives.CharLoader;
import com.dfsek.tectonic.loading.loaders.primitives.DoubleLoader;
import com.dfsek.tectonic.loading.loaders.primitives.FloatLoader;
import com.dfsek.tectonic.loading.loaders.primitives.IntLoader;
import com.dfsek.tectonic.loading.loaders.primitives.LongLoader;
import com.dfsek.tectonic.loading.loaders.primitives.ShortLoader;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class to load a config using reflection magic.
 */
public class ConfigLoader implements TypeRegistry {
    private final Map<Type, TypeLoader<?>> loaders = new HashMap<>();
    private static final Map<Class<?>, Class<?>> primitives = new HashMap<>(); // Map of primitives to their wrapper classes.

    static {
        primitives.put(boolean.class, Boolean.class);
        primitives.put(byte.class, Byte.class);
        primitives.put(short.class, Short.class);
        primitives.put(char.class, Character.class);
        primitives.put(int.class, Integer.class);
        primitives.put(long.class, Long.class);
        primitives.put(float.class, Float.class);
        primitives.put(double.class, Double.class);
        primitives.put(void.class, Void.class);
    }

    {
        // Default primitive/wrapper loaders
        registerLoader(boolean.class, new BooleanLoader());
        registerLoader(Boolean.class, new BooleanLoader());

        registerLoader(byte.class, new ByteLoader());
        registerLoader(Byte.class, new ByteLoader());

        registerLoader(short.class, new ShortLoader());
        registerLoader(Short.class, new ShortLoader());

        registerLoader(char.class, new CharLoader());
        registerLoader(Character.class, new CharLoader());

        registerLoader(int.class, new IntLoader());
        registerLoader(Integer.class, new IntLoader());

        registerLoader(long.class, new LongLoader());
        registerLoader(Long.class, new LongLoader());

        registerLoader(float.class, new FloatLoader());
        registerLoader(Float.class, new FloatLoader());

        registerLoader(double.class, new DoubleLoader());
        registerLoader(Double.class, new DoubleLoader());

        // Default class loaders
        registerLoader(String.class, new StringLoader());

        registerLoader(ArrayList.class, new ArrayListLoader());
        registerLoader(List.class, new ArrayListLoader()); // Lists will default to ArrayList.

        registerLoader(HashMap.class, new HashMapLoader());
        registerLoader(Map.class, new HashMapLoader()); // Maps will default to HashMap.

        registerLoader(HashSet.class, new HashSetLoader());
        registerLoader(Set.class, new HashSetLoader()); // Sets will default to HashSet.

        registerLoader(Duration.class, new DurationLoader());
    }

    /**
     * Register a custom class loader for a type
     *
     * @param t      Type
     * @param loader Loader to load type with
     * @return This config loader
     */
    public ConfigLoader registerLoader(Type t, TypeLoader<?> loader) {
        loaders.put(t, loader);
        return this;
    }

    /**
     * Load a config from an InputStream and put it on a ConfigTemplate object.
     *
     * @param config ConfigTemplate object to put the config on.
     * @param i      InputStream to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, InputStream i) throws ConfigException {
        Configuration configuration = new Configuration(i);
        load(config, configuration);
    }

    /**
     * Load a config from an InputStream and put it on a ConfigTemplate object.
     *
     * @param config ConfigTemplate object to put the config on.
     * @param yaml   YAML string to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, String yaml) throws ConfigException {
        Configuration configuration = new Configuration(yaml);
        load(config, configuration);
    }

    /**
     * Put a {@link Configuration} on a ConfigTemplate object.
     *
     * @param config        ConfigTemplate to put config on.
     * @param configuration Configuration to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, Configuration configuration, AbstractValueProvider provider) throws ConfigException {
        for(Field field : config.getClass().getDeclaredFields()) {
            int m = field.getModifiers();
            if(Modifier.isFinal(m) || Modifier.isStatic(m)) continue; // Don't mess with static/final fields.
            if(!field.isAccessible()) field.setAccessible(true); // Make field accessible if it isn't.
            boolean abstractable = false;
            boolean defaultable = false;
            Value value = null;
            for(Annotation annotation : field.getAnnotations()) {
                if(annotation instanceof Abstractable) abstractable = true;
                if(annotation instanceof Default) defaultable = true;
                if(annotation instanceof Value) value = (Value) annotation;
            }
            if(value != null) {
                Type type = field.getGenericType();
                Type raw = type;
                if(type instanceof ParameterizedType) raw = ((ParameterizedType) type).getRawType();

                Object o;
                if(configuration.contains(value.value())) {
                    if(loaders.containsKey(raw)) o = loadType(type, configuration.get(value.value()));
                    else o = configuration.get(value.value());

                    try {
                        field.set(config, primitives.getOrDefault(field.getType(), field.getType()).cast(o)); // Use primitive wrapper classes if available.
                    } catch(IllegalAccessException e) {
                        throw new ReflectiveAccessException("Failed to set field " + field + ".", e);
                    }
                } else if(!defaultable) {
                    if(!abstractable)
                        throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config."); // Throw exception if value is not provided, and isn't abstractable
                    if(provider == null)
                        throw new ProviderMissingException("Attempted to load abstract value with no abstract provider registered"); // Throw exception if value is abstract and no provider is registered.
                    try {
                        Object abs = provider.get(value.value());
                        if(abs == null)
                            throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config, or its parents."); // Throw exception if value is not provided, and isn't in parents.
                        field.set(config, primitives.getOrDefault(field.getType(), field.getType()).cast(abs)); // Use primitive wrapper classes if available.
                    } catch(IllegalAccessException e) {
                        throw new ReflectiveAccessException("Failed to set field " + field + ".", e);
                    }
                }
            }
        }
    }

    /**
     * Put a {@link Configuration} on a ConfigTemplate object.
     *
     * @param config        ConfigTemplate to put config on.
     * @param configuration Configuration to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, Configuration configuration) throws ConfigException {
        load(config, configuration, null);
    }

    /**
     * Load an Object using the {@link TypeLoader}s registered with this ConfigTemplate.
     *
     * @param t Type of object to load
     * @param o Object to pass to TypeLoader
     * @return Loaded object.
     * @throws LoadException If object could not be loaded.
     */
    public Object loadType(Type t, Object o) throws LoadException {
        Type raw = t;
        if(t instanceof ParameterizedType) raw = ((ParameterizedType) t).getRawType();
        if(loaders.containsKey(raw)) return loaders.get(raw).load(t, o, this);
        else return o;
    }
}
