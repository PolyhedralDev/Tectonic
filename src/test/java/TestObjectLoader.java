import com.dfsek.polyconfig.Configuration;
import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.ClassLoader;

import java.lang.reflect.Type;
import java.util.Map;

public class TestObjectLoader implements ClassLoader<TestObject> {
    @SuppressWarnings("unchecked")
    @Override
    public TestObject load(Type t, Object c, ConfigLoader loader) {
        Configuration configuration = new Configuration((Map<String, Object>) c);
        return new TestObject((String) loader.loadType(String.class, configuration.get("string")), (int) loader.loadType(int.class, configuration.get("number")));
    }
}
