package com.dfsek.tectonic.impl.abstraction.proto;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.loader.Prototype;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ConfigPrototype extends Prototype {
    @Value("id")
    @Final
    private String id;
    @SuppressWarnings("FieldMayBeFinal")
    @Value("extends")
    @Final
    @Default
    private List<String> extend = Collections.emptyList();

    @SuppressWarnings("FieldMayBeFinal")
    @Value("abstract")
    @Final
    @Default
    private boolean isAbstract = false;


    public ConfigPrototype(Configuration config) throws ConfigException {
        super(config);
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public @NotNull String getID() {
        return id;
    }

    @Override
    public @NotNull List<String> getExtends() {
        return extend;
    }
}
