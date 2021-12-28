package com.dfsek.tectonic.api.preprocessor;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

public interface ValuePreprocessor<A extends Annotation> {
    @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, A annotation, DepthTracker tracker);
}
