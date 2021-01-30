package com.dfsek.tectonic.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

public class ReflectionUtil {
    public static Field[] getFields(@NotNull Class<?> type) {
        Field[] result = type.getDeclaredFields();
        Class<?> parentClass = type.getSuperclass();
        if(parentClass != null) {
            result = Stream.concat(Arrays.stream(result), Arrays.stream(getFields(parentClass))).toArray(Field[]::new);
        }
        return result;
    }
}
