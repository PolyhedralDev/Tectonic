package com.dfsek.tectonic.preprocessor;

import org.jetbrains.annotations.Nullable;

public class Result<T> {
    private final TransformType type;
    private final T overwritten;
    private final T transformed;

    private Result(@Nullable T overwritten, @Nullable T transformed, TransformType type) {
        this.type = type;
        this.overwritten = overwritten;
        this.transformed = transformed;
    }

    public static <T> Result<T> overwrite(T result) {
        return new Result<>(result, null, TransformType.OVERWRITE);
    }

    public static <T> Result<T> fail() {
        return new Result<>(null, null, TransformType.FAIL);
    }

    public static <T> Result<T> transform(T transformed) {
        return new Result<>(null, transformed, TransformType.TRANSFORM);
    }

    protected enum TransformType {
        OVERWRITE,
        TRANSFORM,
        FAIL
    }
}
