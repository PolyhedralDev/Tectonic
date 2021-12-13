package com.dfsek.tectonic.util;

import com.dfsek.tectonic.api.exception.ReflectiveAccessException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ReflectionUtil {
    private static final Map<Class<?>, Class<?>> PRIMITIVES = new HashMap<>(); // Map of primitives to their wrapper classes.

    static {
        PRIMITIVES.put(boolean.class, Boolean.class);
        PRIMITIVES.put(byte.class, Byte.class);
        PRIMITIVES.put(short.class, Short.class);
        PRIMITIVES.put(char.class, Character.class);
        PRIMITIVES.put(int.class, Integer.class);
        PRIMITIVES.put(long.class, Long.class);
        PRIMITIVES.put(float.class, Float.class);
        PRIMITIVES.put(double.class, Double.class);
        PRIMITIVES.put(void.class, Void.class);
    }

    public static Field[] getFields(@NotNull Class<?> type) {
        Field[] result = type.getDeclaredFields();
        Class<?> parentClass = type.getSuperclass();
        if(parentClass != null) {
            result = Stream.concat(Arrays.stream(result), Arrays.stream(getFields(parentClass))).toArray(Field[]::new);
        }
        return result;
    }

    /**
     * Cast an object to a class, using primitive wrappers if available.
     *
     * @param clazz  Class to cast to
     * @param object Object to cast
     * @return Cast object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Class<T> clazz, Object object) {
        return (T) PRIMITIVES.getOrDefault(clazz, clazz).cast(object);
    }

    /**
     * Set a field on an object to a value, and wrap any exceptions in a {@link ReflectiveAccessException}
     *
     * @param field  Field to set.
     * @param target Object to set field on.
     * @param value  Value of field.
     */
    public static void setField(Field field, Object target, Object value) throws ReflectiveAccessException {
        try {
            field.set(target, value);
        } catch(IllegalAccessException e) {
            throw new ReflectiveAccessException("Failed to set field " + field + ".", e);
        }
    }
}
