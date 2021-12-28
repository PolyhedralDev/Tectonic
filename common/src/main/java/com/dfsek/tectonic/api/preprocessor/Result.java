package com.dfsek.tectonic.api.preprocessor;

import com.dfsek.tectonic.api.depth.DepthTracker;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Result<T> implements UnaryOperator<T> {
    private final TransformType type;
    private final T overwritten;
    private final Consumer<T> transformer;
    private final DepthTracker tracker;

    private Result(T overwritten, Consumer<T> transformer, TransformType type, DepthTracker tracker) {
        this.type = type;
        this.overwritten = overwritten;
        this.transformer = transformer;
        this.tracker = tracker;
    }

    public static <T> Result<T> overwrite(T result, DepthTracker tracker) {
        return new Result<>(result, null, TransformType.OVERWRITE, tracker);
    }

    public static <T> Result<T> noOp() {
        return new Result<>(null, null, TransformType.NOP, null);
    }

    public static <T> Result<T> transform(@NotNull Consumer<T> transformer, DepthTracker tracker) {
        Objects.requireNonNull(transformer);
        return new Result<>(null, transformer, TransformType.TRANSFORM, tracker);
    }

    public static <T> Result<T> transform(@NotNull Consumer<T> transformer) {
        Objects.requireNonNull(transformer);
        return new Result<>(null, transformer, TransformType.TRANSFORM, null);
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

    public DepthTracker getTracker(DepthTracker original) {
        if(tracker == null) {
            return original;
        }
        return tracker;
    }

    private enum TransformType {
        OVERWRITE,
        TRANSFORM,
        NOP
    }
}
