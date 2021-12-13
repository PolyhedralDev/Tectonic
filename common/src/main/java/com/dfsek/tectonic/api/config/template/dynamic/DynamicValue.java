package com.dfsek.tectonic.api.config.template.dynamic;

import com.dfsek.tectonic.util.ClassAnnotatedTypeImpl;

import java.lang.reflect.AnnotatedType;

public class DynamicValue<T> {
    private final boolean isFinal;

    private final boolean isDefault;
    private final T defaultValue;
    private final Class<T> type;
    private final AnnotatedType annotatedType;
    private final String key;

    public DynamicValue(boolean isFinal, boolean isDefault, T defaultValue, Class<T> type, AnnotatedType annotatedType, String key) {
        this.isFinal = isFinal;
        this.isDefault = isDefault;
        this.defaultValue = defaultValue;
        this.type = type;
        this.annotatedType = annotatedType;
        this.key = key;
    }

    public Class<T> getType() {
        return type;
    }

    public AnnotatedType getAnnotatedType() {
        return annotatedType;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public String getKey() {
        return key;
    }

    public static <T> Builder<T> builder(String key, Class<T> type) {
        return new Builder<>(type, key);
    }

    public static final class Builder<T> {
        private final Class<T> clazz;

        private AnnotatedType annotatedType;
        private boolean isFinal = false;
        private boolean isDefault = false;
        private T defaultValue = null;

        private final String key;
        private Builder(Class<T> clazz, String key) {
            this.clazz = clazz;
            this.key = key;
        }

        public Builder<T> annotatedType(AnnotatedType type) {
            this.annotatedType = type;
            return this;
        }

        public Builder<T> setFinal() {
            this.isFinal = true;
            return this;
        }

        public Builder<T> setDefault(T defaultValue) {
            this.defaultValue = defaultValue;
            this.isDefault = true;
            return this;
        }

        public DynamicValue<T> build() {
            if(annotatedType == null) {
                annotatedType = new ClassAnnotatedTypeImpl(clazz);
            }
            return new DynamicValue<>(isFinal, isDefault, defaultValue, clazz, annotatedType, key);
        }
    }
}
