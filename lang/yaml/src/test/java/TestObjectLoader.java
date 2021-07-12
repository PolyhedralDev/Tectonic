import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

public class TestObjectLoader implements TypeLoader<TestObject> {
    @SuppressWarnings("unchecked")
    @Override
    public TestObject load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Configuration configuration = new YamlConfiguration((Map<String, Object>) c);
        return new TestObject(loader.loadType(String.class, configuration.get("string")), (int) loader.loadType(int.class, configuration.get("number")));
    }
}
