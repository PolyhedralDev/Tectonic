import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.impl.MapConfiguration;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Collections;
import java.util.Map;

public class TestObjectLoader implements TypeLoader<TestObject> {
    @SuppressWarnings("unchecked")
    @Override
    public TestObject load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) throws LoadException {
        Configuration configuration = new MapConfiguration((Map<String, Object>) c);
        return new TestObject(loader.loadType(String.class, configuration.get("string"), depthTracker.entry("string")),
                loader.loadType(int.class, configuration.get("number"), depthTracker.entry("number")),
                Collections.emptyList());
    }
}
