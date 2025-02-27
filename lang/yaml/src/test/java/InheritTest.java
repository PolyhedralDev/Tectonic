import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;


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
