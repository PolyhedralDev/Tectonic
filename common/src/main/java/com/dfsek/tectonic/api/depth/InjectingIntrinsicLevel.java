package com.dfsek.tectonic.api.depth;

import java.util.Optional;

public interface InjectingIntrinsicLevel {
    Optional<String> inject(Level level);
}
