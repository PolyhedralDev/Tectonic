import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ConfigTest {
    @Test
    public void config() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(TestObject.class, new TestObjectLoader());
        ExampleConfig example = new ExampleConfig();
        loader.load(example, new YamlConfiguration(this.getClass().getResourceAsStream("/test.yml")));
        System.out.println(example.getString1());
        System.out.println(example.getString());
        System.out.println(example.getNestedString());
        System.out.println(example.getNumber());
        System.out.println(example.getTestObject());
        System.out.println();
        for(String s : example.getList()) {
            System.out.print(s + ", ");
        }
        System.out.println();
        for(Map.Entry<String, String> e : example.getMap().entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
        System.out.println();
        for(Map.Entry<String, TestObject> e : example.getObjectHashMap().entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue().getString());
        }
    }
}
