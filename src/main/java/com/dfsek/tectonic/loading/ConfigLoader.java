package com.dfsek.tectonic.loading;

import com.dfsek.tectonic.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.abstraction.exception.ProviderMissingException;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.config.YamlConfiguration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.InvalidTemplateException;
import com.dfsek.tectonic.exception.LoadException;
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
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.tectonic.loading.object.ObjectTemplateLoader;
import com.dfsek.tectonic.preprocessor.ValuePreprocessor;
import com.dfsek.tectonic.util.ReflectionUtil;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
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
import java.util.function.Supplier;

/**
 * Class to load a config using reflection magic.
 */
public class ConfigLoader implements TypeRegistry {
    private final Map<Type, TypeLoader<?>> loaders = new HashMap<>();
    private final Map<Class<? extends Annotation>, List<ValuePreprocessor<?>>> preprocessors = new HashMap<>();

    public ConfigLoader() {
        // Default primitive/wrapper loaders
        BooleanLoader booleanLoader = new BooleanLoader();
        registerLoader(boolean.class, booleanLoader);
        registerLoader(Boolean.class, booleanLoader);

        ByteLoader byteLoader = new ByteLoader();
        registerLoader(byte.class, byteLoader);
        registerLoader(Byte.class, byteLoader);

        ShortLoader shortLoader = new ShortLoader();
        registerLoader(short.class, shortLoader);
        registerLoader(Short.class, shortLoader);

        CharLoader charLoader = new CharLoader();
        registerLoader(char.class, charLoader);
        registerLoader(Character.class, charLoader);

        IntLoader intLoader = new IntLoader();
        registerLoader(int.class, intLoader);
        registerLoader(Integer.class, intLoader);

        LongLoader longLoader = new LongLoader();
        registerLoader(long.class, longLoader);
        registerLoader(Long.class, longLoader);

        FloatLoader floatLoader = new FloatLoader();
        registerLoader(float.class, floatLoader);
        registerLoader(Float.class, floatLoader);

        DoubleLoader doubleLoader = new DoubleLoader();
        registerLoader(double.class, doubleLoader);
        registerLoader(Double.class, doubleLoader);

        // Default class loaders
        registerLoader(String.class, new StringLoader());

        ArrayListLoader arrayListLoader = new ArrayListLoader();
        registerLoader(ArrayList.class, arrayListLoader);
        registerLoader(List.class, arrayListLoader); // Lists will default to ArrayList.

        HashMapLoader hashMapLoader = new HashMapLoader();
        registerLoader(HashMap.class, hashMapLoader);
        registerLoader(Map.class, hashMapLoader); // Maps will default to HashMap.

        HashSetLoader hashSetLoader = new HashSetLoader();
        registerLoader(HashSet.class, hashSetLoader);
        registerLoader(Set.class, hashSetLoader); // Sets will default to HashSet.

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

    public <T> ConfigLoader registerLoader(Type t, Supplier<ObjectTemplate<T>> provider) {
        loaders.put(t, new ObjectTemplateLoader<>(provider));
        return this;
    }

    public <T extends Annotation> ConfigLoader registerPreprocessor(Class<? extends T> clazz, ValuePreprocessor<T> processor) {
        preprocessors.computeIfAbsent(clazz, c -> new ArrayList<>()).add(processor);
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
            Configuration configuration = new YamlConfiguration(i);
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
            Configuration configuration = new YamlConfiguration(yaml);
            load(config, configuration);
        } catch(YAMLException e) {
            throw new LoadException("Failed to parse YAML: " + e.getMessage(), e);
        }
    }

    /**
     * Load a {@link YamlConfiguration} to a ConfigTemplate object.
     *
     * @param config        ConfigTemplate to put config on.
     * @param configuration Configuration to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, Configuration configuration, AbstractConfiguration provider) throws ConfigException {

    }

    private Object getFinal(Configuration configuration, String key) {
        if(configuration instanceof AbstractConfiguration) return ((AbstractConfiguration) configuration).getBase(key);
        return configuration.get(key);
    }

    /**
     * Load a {@link YamlConfiguration} to a ConfigTemplate object.
     *
     * @param config        ConfigTemplate to put config on.
     * @param configuration Configuration to load from.
     * @throws ConfigException If config cannot be loaded.
     */
    public void load(ConfigTemplate config, Configuration configuration) throws ConfigException {
        for(Field field : ReflectionUtil.getFields(config.getClass())) {
            int m = field.getModifiers();
            Value value = field.getAnnotation(Value.class);
            if(value == null) continue;

            if(Modifier.isFinal(m) || Modifier.isStatic(m)) {
                throw new InvalidTemplateException("Field annotated @Value cannot be static or final: " + field.getName() + " of " + config.getClass().getCanonicalName());
            }

            field.setAccessible(true); // Make field accessible so we can mess with it.

            boolean abstractable = !field.isAnnotationPresent(Final.class);
            boolean defaultable = field.isAnnotationPresent(Default.class);

            AnnotatedType type = field.getAnnotatedType();
            Type raw = type.getType();
            if(type instanceof AnnotatedParameterizedType) raw = ((ParameterizedType) type.getType()).getRawType();

            try {
                if(configuration.contains(value.value())) { // If config contains value, load it.
                    Object loadedObject = getFinal(configuration, value.value()); // Assign raw config object retrieved.
                    if(loaders.containsKey(raw)) {
                        loadedObject = loadType(type, loadedObject); // Re-assign if type is found in registry.
                    }
                    ReflectionUtil.setField(field, config, ReflectionUtil.cast(field.getType(), loadedObject)); // Set the field to the loaded value.
                } else if(abstractable) { // If value is abstractable, try to get it from parent configs.
                    if(!(config instanceof AbstractConfiguration)) {
                        throw new ProviderMissingException("Attempted to load abstract value in non-abstractable context.");
                    }
                    Object abs = configuration.get(value.value());
                    if(abs == null) {
                        if(defaultable) continue;
                        throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config, or its parents: " + configuration.getName()); // Throw exception if value is not provided, and isn't in parents.
                    }
                    abs = loadType(type, abs);
                    ReflectionUtil.setField(field, config, ReflectionUtil.cast(field.getType(), abs));
                } else if(!defaultable) {
                    throw new ValueMissingException("Value \"" + value.value() + "\" was not found in the provided config: " + configuration.getName()); // Throw exception if value is not provided, and isn't abstractable
                }
            } catch(Exception e) {
                throw new LoadException("Failed to load value \"" + value.value() + "\" to field \"" + field.getName() + "\" in config \"" + configuration.getName() + "\": " + e.getMessage(), e);
            }
        }

        if(config instanceof ValidatedConfigTemplate
                && !((ValidatedConfigTemplate) config).validate()) {
            throw new ValidationException("Failed to validate config. Reason unspecified:" + configuration.getName());
        }
    }

    /**
     * Load an Object using the {@link TypeLoader}s registered with this ConfigTemplate.
     *
     * @param t Type of object to load
     * @param o Object to pass to TypeLoader
     * @return Loaded object.
     * @throws LoadException If object could not be loaded.
     */
    public Object loadType(AnnotatedType t, Object o) throws LoadException {
        try {
            Type raw = t.getType();
            if(t instanceof AnnotatedParameterizedType) raw = ((ParameterizedType) t.getType()).getRawType();
            if(loaders.containsKey(raw)) return loaders.get(raw).load(t, o, this);
            else return o;
        } catch(LoadException e) { // Rethrow LoadExceptions.
            throw e;
        } catch(Exception e) { // Catch, wrap, and rethrow exception.
            throw new LoadException("Unexpected exception thrown during type loading: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T loadType(Class<T> clazz, Object o) throws LoadException {
        try {
            if(loaders.containsKey(clazz)) return ReflectionUtil.cast(clazz, ((TypeLoader<Object>) loaders.get(clazz)).load((Class<Object>) clazz, o, this));
            else return ReflectionUtil.cast(clazz, o);
        } catch(LoadException e) { // Rethrow LoadExceptions.
            throw e;
        } catch(Exception e) { // Catch, wrap, and rethrow exception.
            throw new LoadException("Unexpected exception thrown during type loading: " + e.getMessage(), e);
        }
    }
}
