package com.dfsek.tectonic.impl.loading;

import com.dfsek.tectonic.impl.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.loader.TypeLoader;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.exception.InvalidTemplateException;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.exception.ValidationException;
import com.dfsek.tectonic.api.exception.ValueMissingException;
import com.dfsek.tectonic.impl.loading.loaders.StringLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.LongLoader;
import com.dfsek.tectonic.impl.loading.loaders.EnumLoader;
import com.dfsek.tectonic.impl.loading.loaders.generic.ArrayListLoader;
import com.dfsek.tectonic.impl.loading.loaders.generic.HashMapLoader;
import com.dfsek.tectonic.impl.loading.loaders.generic.HashSetLoader;
import com.dfsek.tectonic.impl.loading.loaders.other.DurationLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.BooleanLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.ByteLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.CharLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.DoubleLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.FloatLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.IntLoader;
import com.dfsek.tectonic.impl.loading.loaders.primitives.ShortLoader;
import com.dfsek.tectonic.api.ObjectTemplate;
import com.dfsek.tectonic.impl.loading.object.ObjectTemplateLoader;
import com.dfsek.tectonic.api.preprocessor.ValuePreprocessor;
import com.dfsek.tectonic.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;

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
    private static final EnumLoader ENUM_LOADER = new EnumLoader();
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

        registerLoader(Enum.class, ENUM_LOADER);
    }

    /**
     * Register a custom class loader for a type
     *
     * @param t      Type
     * @param loader Loader to load type with
     * @return This config loader
     */
    public @NotNull ConfigLoader registerLoader(@NotNull Type t, @NotNull TypeLoader<?> loader) {
        loaders.put(t, loader);
        return this;
    }

    public <T> @NotNull ConfigLoader registerLoader(@NotNull Type t, @NotNull Supplier<ObjectTemplate<T>> provider) {
        loaders.put(t, new ObjectTemplateLoader<>(provider));
        return this;
    }

    public <T extends Annotation> ConfigLoader registerPreprocessor(Class<? extends T> clazz, ValuePreprocessor<T> processor) {
        preprocessors.computeIfAbsent(clazz, c -> new ArrayList<>()).add(processor);
        return this;
    }

    private Object getFinal(Configuration configuration, String key) {
        if(configuration instanceof AbstractConfiguration) return ((AbstractConfiguration) configuration).getBase(key);
        return configuration.get(key);
    }

    private boolean containsFinal(Configuration configuration, String key) {
        if(configuration instanceof AbstractConfiguration)
            return ((AbstractConfiguration) configuration).containsBase(key);
        return configuration.contains(key);
    }

    /**
     * Load a {@link Configuration} to a ConfigTemplate object.
     *
     * @param config        ConfigTemplate to put config on.
     * @param configuration Configuration to load from.
     * @return The loaded configuration. <b>not</b> a new instance.
     * @throws ConfigException If config cannot be loaded.
     */
    public <T extends ConfigTemplate> T load(T config, Configuration configuration) throws ConfigException {
        for(Field field : ReflectionUtil.getFields(config.getClass())) {
            int m = field.getModifiers();
            Value value = field.getAnnotation(Value.class);
            if(value == null) continue;

            if(Modifier.isFinal(m) || Modifier.isStatic(m)) {
                throw new InvalidTemplateException("Field annotated @Value cannot be static or final: " + field.getName() + " of " + config.getClass().getCanonicalName());
            }

            field.setAccessible(true); // Make field accessible so we can mess with it.

            boolean isFinal = field.isAnnotationPresent(Final.class);
            boolean defaultable = field.isAnnotationPresent(Default.class);

            AnnotatedType type = field.getAnnotatedType();

            try {
                if(containsFinal(configuration, value.value())) { // If config contains value, load it.
                    Object loadedObject = loadType(type, getFinal(configuration, value.value())); // Re-assign if type is found in registry.
                    ReflectionUtil.setField(field, config, ReflectionUtil.cast(field.getType(), loadedObject)); // Set the field to the loaded value.
                } else if(!isFinal && (configuration instanceof AbstractConfiguration)) { // If value is abstractable, try to get it from parent configs.
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
        return config;
    }

    /**
     * Load an Object using the {@link TypeLoader}s registered with this ConfigTemplate.
     *
     * @param t Type of object to load
     * @param o Object to pass to TypeLoader
     * @return Loaded object.
     * @throws LoadException If object could not be loaded.
     */
    @SuppressWarnings("unchecked")
    public Object loadType(AnnotatedType t, Object o) throws LoadException {
        for(Annotation annotation : t.getAnnotations()) {
            if(preprocessors.containsKey(annotation.annotationType())) {
                for(ValuePreprocessor<?> preprocessor : preprocessors.get(annotation.annotationType())) {
                    o = ((ValuePreprocessor<Annotation>) preprocessor).process(t, o, this, annotation).apply(o);
                }
            }
        }
        return getObject(t, o);
    }

    private Object getObject(AnnotatedType t, Object o) throws LoadException {
        try {
            Type raw = t.getType();
            if(loaders.containsKey(raw)) return loaders.get(raw).load(t, o, this);
            if(t instanceof AnnotatedParameterizedType) {
                raw = ((ParameterizedType) t.getType()).getRawType();
                if(loaders.containsKey(raw)) return loaders.get(raw).load(t, o, this);
            }

            if(raw instanceof Class && ((Class<?>) raw).isEnum()) {
                return ENUM_LOADER.load(t, o, this); // Special enum loader if enum doesn't already have loader defined.
            }

            throw new LoadException("No loaders are registered for type " + t.getType().getTypeName());
        } catch(LoadException e) { // Rethrow LoadExceptions.
            throw e;
        } catch(Exception e) { // Catch, wrap, and rethrow exception.
            throw new LoadException("Unexpected exception thrown during type loading: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T loadType(Class<T> clazz, Object o) throws LoadException {
        try {
            if(loaders.containsKey(clazz))
                return ReflectionUtil.cast(clazz, ((TypeLoader<Object>) loaders.get(clazz)).load((Class<Object>) clazz, o, this));
            else return ReflectionUtil.cast(clazz, o);
        } catch(LoadException e) { // Rethrow LoadExceptions.
            throw e;
        } catch(Exception e) { // Catch, wrap, and rethrow exception.
            throw new LoadException("Unexpected exception thrown during type loading: " + e.getMessage(), e);
        }
    }
}
