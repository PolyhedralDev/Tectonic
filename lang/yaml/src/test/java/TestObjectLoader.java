import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.config.MapConfiguration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
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
