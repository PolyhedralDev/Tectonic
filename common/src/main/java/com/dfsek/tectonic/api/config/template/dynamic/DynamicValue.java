package com.dfsek.tectonic.api.config.template.dynamic;

public class DynamicValue<T> {
    private final boolean isFinal;

    private final boolean isDefault;
    private final T defaultValue;
    private final Class<T> type;
    private final String key;

    public DynamicValue(boolean isFinal, boolean isDefault, T defaultValue, Class<T> type, String key) {
        this.isFinal = isFinal;
        this.isDefault = isDefault;
        this.defaultValue = defaultValue;
        this.type = type;
        this.key = key;
    }

    public Class<T> getType() {
        return type;
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
        private boolean isFinal = false;
        private boolean isDefault = false;
        private T defaultValue = null;

        private final String key;
        private Builder(Class<T> clazz, String key) {
            this.clazz = clazz;
            this.key = key;
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
            return new DynamicValue<>(isFinal, isDefault, defaultValue, clazz, key);
        }
    }
}
