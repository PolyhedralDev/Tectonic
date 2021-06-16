package com.dfsek.tectonic.preprocessor;

import com.dfsek.tectonic.loading.ConfigLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface ValuePreprocessor<A extends Annotation> {
    <T> Result<T> process(Type t, T c, ConfigLoader loader, A annotation);
}
