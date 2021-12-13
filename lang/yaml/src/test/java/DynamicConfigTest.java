import com.dfsek.tectonic.api.config.template.dynamic.DynamicTemplate;
import com.dfsek.tectonic.api.config.template.dynamic.DynamicValue;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

public class DynamicConfigTest {
    @Test
    public void testDynamicConfig() {
        DynamicTemplate template = DynamicTemplate
                .builder()
                .value("val1", DynamicValue
                        .builder("value1", String.class)
                        .setFinal()
                        .build())
                .value("val2", DynamicValue
                        .builder("number", double.class)
                        .setFinal()
                        .build())
                .build();

        ConfigLoader loader = new ConfigLoader();
        loader.load(template, new YamlConfiguration(TypeTest.class.getResourceAsStream("/test.yml")));

        System.out.println(template.get("val2", double.class));
        System.out.println(template.get("val1", String.class));
    }
}
