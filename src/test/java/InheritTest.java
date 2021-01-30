import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

public class InheritTest implements ConfigTemplate {
    @Value("value1")
    private String string1;

    @Value("a.b.c.d.e.f")
    private String nestedString;

    public String getString1() {
        return string1;
    }

    public String getNestedString() {
        return nestedString;
    }
}
