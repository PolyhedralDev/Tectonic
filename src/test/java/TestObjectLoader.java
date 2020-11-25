import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;
import java.util.Map;

public class TestObjectLoader implements TypeLoader<TestObject> {
    @SuppressWarnings("unchecked")
    @Override
    public TestObject load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Configuration configuration = new Configuration((Map<String, Object>) c);
        return new TestObject((String) loader.loadType(String.class, configuration.get("string")), (int) loader.loadType(int.class, configuration.get("number")));
    }
}
