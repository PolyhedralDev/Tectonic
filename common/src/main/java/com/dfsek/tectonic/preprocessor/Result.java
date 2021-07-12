package com.dfsek.tectonic.preprocessor;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Result<T> implements UnaryOperator<T> {
    private final TransformType type;
    private final T overwritten;
    private final Consumer<T> transformer;

    private Result(T overwritten, Consumer<T> transformer, TransformType type) {
        this.type = type;
        this.overwritten = overwritten;
        this.transformer = transformer;
    }

    public static <T> Result<T> overwrite(T result) {
        return new Result<>(result, null, TransformType.OVERWRITE);
    }

    public static <T> Result<T> noOp() {
        return new Result<>(null, null, TransformType.NOP);
    }

    public static <T> Result<T> transform(@NotNull Consumer<T> transformer) {
        Objects.requireNonNull(transformer);
        return new Result<>(null, transformer, TransformType.TRANSFORM);
    }

    @Override
    public T apply(T t) {
        switch(type) {
            case NOP:
                return t;
            case OVERWRITE:
                return overwritten;
            case TRANSFORM:
                transformer.accept(t);
                return t;
        }
        return t;
    }

    private enum TransformType {
        OVERWRITE,
        TRANSFORM,
        NOP
    }
}
