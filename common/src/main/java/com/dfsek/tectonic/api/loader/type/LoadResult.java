package com.dfsek.tectonic.api.loader.type;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LoadResult<T> {
    private final String errorMessage;
    private final T value;
    private LoadResult(String errorMessage, T value) {
        this.errorMessage = errorMessage;

        this.value = value;
    }

    public Optional<String> errorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    public Optional<T> value() {
        return Optional.ofNullable(value);
    }


    @SuppressWarnings("unchecked")
    public <U> LoadResult<U> map(Function<T, U> map) {
        if(value != null) {
            return new LoadResult<>(null, map.apply(value));
        }
        return (LoadResult<U>) this;
    }

    public static <T> LoadResult<T> ofTry(Supplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch(Exception e) {
            return fail(e.getMessage(), e);
        }
    }

    public static <T> LoadResult<T> success(T value) {
        return new LoadResult<>(null, value);
    }

    public static <T> LoadResult<T> fail(String message) {
        return new LoadResult<>(Objects.requireNonNull(message), null);
    }

    public static <T> LoadResult<T> fail(String message, Exception e) {
        return new LoadResult<>(Objects.requireNonNull(message), null);
    }
}
