package com.dfsek.polyconfig.loading;

import com.dfsek.polyconfig.ConfigTemplate;
import com.dfsek.polyconfig.Configuration;
import com.dfsek.polyconfig.abstraction.AbstractValueProvider;
import com.dfsek.polyconfig.annotations.Abstractable;
import com.dfsek.polyconfig.annotations.Default;
import com.dfsek.polyconfig.annotations.Value;
import com.dfsek.polyconfig.exception.ConfigException;
import com.dfsek.polyconfig.exception.LoadException;
import com.dfsek.polyconfig.exception.ReflectiveAccessException;
import com.dfsek.polyconfig.exception.ValueMissingException;
import com.dfsek.polyconfig.loading.loaders.StringLoader;
import com.dfsek.polyconfig.loading.loaders.generic.ArrayListLoader;
import com.dfsek.polyconfig.loading.loaders.generic.HashMapLoader;
import com.dfsek.polyconfig.loading.loaders.generic.HashSetLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.BooleanLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.ByteLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.CharLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.DoubleLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.FloatLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.IntLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.LongLoader;
import com.dfsek.polyconfig.loading.loaders.primitives.ShortLoader;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
    private AbstractValueProvider provider;

    static {
        primitives.put(boolean.class, Boolean.class);
        primitives.put(byte.class, Byte.class);
        primitives.put(short.class, Short.class);
        primitives.put(char.class, Character.class);
        primitives.put(int.class, Integer.class);
        primitives.put(long.class, Long.class);
        primitives.put(float.class, Float.class);
        primitives.put(double.class, Double.class);
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
    public void load(ConfigTemplate config, Configuration configuration) throws ConfigException {
        for(Field field : config.getClass().getDeclaredFields()) {
            int m = field.getModifiers();
            if(Modifier.isFinal(m) || Modifier.isStatic(m)) continue; // Don't mess with static/final fields.
            if(!field.isAccessible()) field.setAccessible(true); // Make field accessible if it isn't.
            boolean abstractable = false;
            boolean defaultable = false;
            Value value = null;
            System.out.println(field);
            for(Annotation annotation : field.getAnnotations()) {
                System.out.println(annotation);
                if(annotation instanceof Abstractable) abstractable = true;
                if(annotation instanceof Default) defaultable = true;
                if(annotation instanceof Value) value = (Value) annotation;
            }
            if(value != null) {
                System.out.println("Loading value...");
                Type type = field.getGenericType();
                Type raw = type;
                if(type instanceof ParameterizedType) raw = ((ParameterizedType) type).getRawType();
                System.out.println(type);

                Object o;
                if(configuration.contains(value.value())) {
                    if(loaders.containsKey(raw)) o = loadType(type, configuration.get(value.value()));
                    else o = configuration.get(value.value());

                    try {
                        field.set(config, primitives.getOrDefault(field.getType(), field.getType()).cast(o)); // Use primitive wrapper classes if available.
                    } catch(IllegalAccessException e) {
                        throw new ReflectiveAccessException("Failed to set field " + field + ".", e);
                    }
                } else if(!defaultable)
                    throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config.");
                System.out.println();

            }
        }
    }

    public void registerAbstractValueProvider(AbstractValueProvider provider) {
        this.provider = provider;
    }

    public Object loadType(Type t, Object o) throws LoadException {
        Type raw = t;
        if(t instanceof ParameterizedType) raw = ((ParameterizedType) t).getRawType();
        return loaders.get(raw).load(t, o, this);
    }
}
