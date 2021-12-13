import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.impl.MapConfiguration;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.impl.loading.ConfigLoader;
import com.dfsek.tectonic.api.loader.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

public class TestObjectLoader implements TypeLoader<TestObject> {
    @SuppressWarnings("unchecked")
    @Override
    public TestObject load(@NotNull AnnotatedType t, @NotNull Object c, ConfigLoader loader) throws LoadException {
        Configuration configuration = new MapConfiguration((Map<String, Object>) c);
        return new TestObject(loader.loadType(String.class, configuration.get("string")), loader.loadType(int.class, configuration.get("number")));
    }
}
