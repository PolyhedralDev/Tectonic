package com.dfsek.tectonic.util;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

public class ClassAnnotatedTypeImpl implements AnnotatedType {
    private final Class<?> clazz;

    public ClassAnnotatedTypeImpl(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Type getType() {
        return clazz;
    }

    @Override
    public <T extends Annotation> T getAnnotation(@NotNull Class<T> aClass) {
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
    }
}
