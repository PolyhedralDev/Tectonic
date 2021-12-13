package com.dfsek.tectonic.api.preprocessor;

import com.dfsek.tectonic.impl.loading.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

public interface ValuePreprocessor<A extends Annotation> {
    @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, A annotation);
}
