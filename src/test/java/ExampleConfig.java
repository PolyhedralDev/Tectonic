import com.dfsek.polyconfig.ConfigTemplate;
import com.dfsek.polyconfig.annotations.Default;
import com.dfsek.polyconfig.annotations.Value;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ExampleConfig implements ConfigTemplate {
    @Default
    @Value("value2")
    private final String string = "test value";

    @Value("value1")
    private String string1;

    @Value("a.b.c.d.e.f")
    private String nestedString;

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

    public String getString1() {
        return string1;
    }

    public String getNestedString() {
        return nestedString;
    }

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
