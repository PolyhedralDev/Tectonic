package com.dfsek.tectonic.config;

import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.ConfigLoader;

/**
 * {@link ConfigTemplate} implementation that supports validation of config values directly after loading.
 */
public interface ValidatedConfigTemplate extends ConfigTemplate {
    /**
     * Check whether the config is valid.
     * <p>
     * This method is invoked by the {@link ConfigLoader} immediately after loading is finished.
     * The Template should then check whether the values loaded are "valid". If they are, this method should return
     * true and pass. If this method returns false, a generic {@link ValidationException} will be thrown. It is
     * recommended that implementations throw their own ValidationExceptions, with more specific information about
     * where validation failed.
     *
     * @return Whether config is valid.
     * @throws ValidationException If validation fails.
     * @see ConfigTemplate
     */
    boolean validate() throws ValidationException;
}
