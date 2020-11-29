package com.dfsek.tectonic.config;

import com.dfsek.tectonic.exception.ValidationException;

public interface ValidatedConfigTemplate extends ConfigTemplate {
    boolean validate() throws ValidationException;
}
