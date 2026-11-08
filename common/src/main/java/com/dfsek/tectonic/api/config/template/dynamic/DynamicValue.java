package com.dfsek.tectonic.api.config.template.dynamic;

import com.dfsek.tectonic.util.ClassAnnotatedTypeImpl;

import java.lang.reflect.AnnotatedType;


public class DynamicValue<T> {
    private final boolean isFinal;
    private final boolean isMerge;

    private final boolean isDefault;
    private final T defaultValue;
    private final Class<T> type;
    private final AnnotatedType annotatedType;
    private final String key;

    public DynamicValue(boolean isFinal, boolean isDefault, T defaultValue, Class<T> type, AnnotatedType annotatedType, String key) {
        this(isFinal, false, isDefault, defaultValue, type, annotatedType, key);
    }

    /**
     * @param isMerge Whether an inherited value is merged across the inheritance tree, like
     *                {@link com.dfsek.tectonic.api.config.template.annotations.Merge}.
     *
     * @throws IllegalStateException if both {@code isFinal} and {@code isMerge} are set.
     */
    public DynamicValue(boolean isFinal, boolean isMerge, boolean isDefault, T defaultValue, Class<T> type, AnnotatedType annotatedType,
                        String key) {
        if(isFinal && isMerge) throw new IllegalStateException("Dynamic value \"" + key + "\" cannot be both merge and final");
        this.isFinal = isFinal;
        this.isMerge = isMerge;
        this.isDefault = isDefault;
        this.defaultValue = defaultValue;
        this.type = type;
        this.annotatedType = annotatedType;
        this.key = key;
    }

    public static <T> Builder<T> builder(String key, Class<T> type) {
        return new Builder<>(type, key);
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

    public boolean isMerge() {
        return isMerge;
    }

    public String getKey() {
        return key;
    }


    public static final class Builder<T> {
        private final Class<T> clazz;
        private final String key;
        private AnnotatedType annotatedType;
        private boolean isFinal = false;
        private boolean isMerge = false;
        private boolean isDefault = false;
        private T defaultValue = null;

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

        /**
         * Merge an inherited value across the inheritance tree, like
         * {@link com.dfsek.tectonic.api.config.template.annotations.Merge}. The value's type must be a
         * {@link java.util.Map} or a {@link java.util.List}, and it cannot also be final.
         *
         * @return This builder.
         */
        public Builder<T> merge() {
            this.isMerge = true;
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
            return new DynamicValue<>(isFinal, isMerge, isDefault, defaultValue, clazz, annotatedType, key);
        }
    }
}
