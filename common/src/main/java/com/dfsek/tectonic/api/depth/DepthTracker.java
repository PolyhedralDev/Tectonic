package com.dfsek.tectonic.api.depth;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public final class DepthTracker {
    private final List<Level> levels;

    @ApiStatus.Internal
    public DepthTracker(List<Level> levels) {
        this.levels = levels;
    }

    public DepthTracker with(Level level) {
        DepthTracker that = new DepthTracker(new ArrayList<>(levels));
        that.levels.add(level);
        return that;
    }

    public DepthTracker index(int index) {
        return with(new IndexLevel(index));
    }

    public DepthTracker entry(String entry) {
        return with(new EntryLevel(entry));
    }

    public String descriptor() {
        StringBuilder builder = new StringBuilder();

        for(int depth = 0; depth < levels.size(); depth++) {
            Level level = levels.get(depth);
            if(depth > 0) {
                builder.append(level.joinDescriptor());
            }
            builder.append(level.identify());
        }
        return builder.toString();
    }
}
