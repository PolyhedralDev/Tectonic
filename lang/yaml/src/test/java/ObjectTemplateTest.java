import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import org.junit.jupiter.api.Test;

public class ObjectTemplateTest {
    @Test
    public void test() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(TestObject.class, TestTemplate::new);

        ExampleConfig example = new ExampleConfig();
        loader.load(example, new YamlConfiguration(this.getClass().getResourceAsStream("/test.yml")));

        System.out.println(example.getTestObject());
    }

    private static class TestTemplate implements ObjectTemplate<TestObject> {
        @Value("string")
        private String string;

        @Value("number")
        private int number;


        @Override
        public TestObject get() {
            return new TestObject(string, number);
        }
    }
}
