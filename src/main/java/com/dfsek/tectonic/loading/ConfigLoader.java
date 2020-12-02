package com.dfsek.tectonic.loading;

import com.dfsek.tectonic.abstraction.AbstractValueProvider;
import com.dfsek.tectonic.abstraction.exception.ProviderMissingException;
import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.exception.ReflectiveAccessException;
import com.dfsek.tectonic.exception.ValidationException;
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
import org.yaml.snakeyaml.error.YAMLException;

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
     * Load a config from an InputStream to a ConfigTemplate object.
     *
     * @param config ConfigTemplate object to put the config on.
     * @param i      InputStream to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, InputStream i) throws ConfigException {
        try {
            Configuration configuration = new Configuration(i);
            load(config, configuration);
        } catch(YAMLException e) {
            throw new LoadException("Failed to parse YAML: " + e.getMessage(), e);
        }
    }

    /**
     * Load a config from an InputStream to a ConfigTemplate object.
     *
     * @param config ConfigTemplate object to put the config on.
     * @param yaml   YAML string to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, String yaml) throws ConfigException {
        try {
            Configuration configuration = new Configuration(yaml);
            load(config, configuration);
        } catch(YAMLException e) {
            throw new LoadException("Failed to parse YAML: " + e.getMessage(), e);
        }
    }

    /**
     * Load a {@link Configuration} to a ConfigTemplate object.
     *
     * @param config        ConfigTemplate to put config on.
     * @param configuration Configuration to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, Configuration configuration, AbstractValueProvider provider) throws ConfigException {
        for(Field field : config.getClass().getDeclaredFields()) {
            int m = field.getModifiers();
            if(Modifier.isFinal(m) || Modifier.isStatic(m)) continue; // Don't mess with static/final fields.
            field.setAccessible(true); // Make field accessible so we can mess with it.
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
                if(type instanceof ParameterizedType)
                    raw = ((ParameterizedType) type).getRawType(); // If type is parameterized, get raw type to check loaders against.

                try {
                    if(configuration.contains(value.value())) { // If config contains value, load it.
                        Object loadedObject = configuration.get(value.value()); // Assign raw config object retrieved.
                        if(loaders.containsKey(raw))
                            loadedObject = loadType(type, loadedObject); // Re-assign if type is found in registry.
                        setField(field, config, cast(field.getType(), loadedObject)); // Set the field to the loaded value.
                    } else if(abstractable) { // If value is abstractable, try to get it from parent configs.
                        if(provider == null)
                            throw new ProviderMissingException("Attempted to load abstract value with no abstract provider registered"); // Throw exception if value is abstract and no provider is registered.
                        Object abs = provider.get(value.value());
                        if(abs == null) {
                            if(defaultable) continue;
                            throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config, or its parents."); // Throw exception if value is not provided, and isn't in parents.
                        }
                        abs = loadType(type, abs);
                        setField(field, config, cast(field.getType(), abs));
                    } else if(!defaultable) {
                        throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config."); // Throw exception if value is not provided, and isn't abstractable
                    }
                } catch(Exception e) {
                    throw new LoadException("Failed to load value \"" + value.value() + "\" to field \"" + field.getName() + "\": " + e.getMessage(), e);
                }
            }
        }
        if(config instanceof ValidatedConfigTemplate
                && provider == null // Validation is handled separately by AbstractConfigLoader.
                && !((ValidatedConfigTemplate) config).validate())
            throw new ValidationException("Failed to validate config. Reason unspecified.");
    }

    /**
     * Cast an object to a class, using primitive wrappers if available.
     *
     * @param clazz  Class to cast to
     * @param object Object to cast
     * @return Cast object.
     */
    private Object cast(Class<?> clazz, Object object) {
        return primitives.getOrDefault(clazz, clazz).cast(object);
    }

    /**
     * Set a field on an object to a value, and wrap any exceptions in a {@link ReflectiveAccessException}
     *
     * @param field  Field to set.
     * @param target Object to set field on.
     * @param value  Value of field.
     */
    private void setField(Field field, Object target, Object value) throws ReflectiveAccessException {
        try {
            field.set(target, value);
        } catch(IllegalAccessException e) {
            throw new ReflectiveAccessException("Failed to set field " + field + ".", e);
        }
    }

    /**
     * Load a {@link Configuration} to a ConfigTemplate object.
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
        try {
            Type raw = t;
            if(t instanceof ParameterizedType) raw = ((ParameterizedType) t).getRawType();
            if(loaders.containsKey(raw)) return loaders.get(raw).load(t, o, this);
            else return o;
        } catch(LoadException e) { // Rethrow LoadExceptions.
            throw e;
        } catch(Exception e) { // Catch, wrap, and rethrow exception.
            throw new LoadException("Unexpected exception thrown during type loading: " + e.getMessage(), e);
        }
    }
}
