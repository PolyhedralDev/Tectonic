package com.dfsek.tectonic.preprocessor;

import com.dfsek.tectonic.loading.ConfigLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

public interface ValuePreprocessor<A extends Annotation> {
    <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, A annotation);
}
