import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class ObjectTemplateTest {
    @Test
    public void test() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(TestObject.class, TestTemplate::new);

        ExampleConfig example = new ExampleConfig();
        loader.load(example, new YamlConfiguration(this.getClass().getResourceAsStream("/test.yml"), "test.yml"));

        System.out.println(example.getTestObject());
    }

    private static class TestTemplate implements ObjectTemplate<TestObject> {
        @Value("string")
        private String string;

        @Value("number")
        private int number;

        @Value("nested")
        @Default
        private List<TestObject> list = Collections.emptyList();

        @Override
        public TestObject get() {
            return new TestObject(string, number, list);
        }
    }
}
