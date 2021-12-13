import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ExampleConfig extends InheritTest implements ConfigTemplate {
    @Default
    @Value("value2")
    private String string = "test value";

    @Value("number")
    private int number;

    @Value("object")
    private TestObject testObject;

    @Value("list")
    private List<String> list;

    @Value("map")
    private Map<String, String> map;

    @Value("objectMap")
    private Map<String, TestObject> objectHashMap;

    public String getString() {
        return string;
    }

    public int getNumber() {
        return number;
    }

    public TestObject getTestObject() {
        return testObject;
    }

    public List<String> getList() {
        return list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public Map<String, TestObject> getObjectHashMap() {
        return objectHashMap;
    }

}
