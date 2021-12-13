import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EnumTest {
    @Test
    public void testEnums() {
        EnumTemplate enumTemplate = new EnumTemplate();
        new ConfigLoader().load(enumTemplate, new YamlConfiguration("test: [TWO]"));
        System.out.println(enumTemplate.test.get(0).getThing());
    }

    public enum TestEnum {
        ONE("1"),
        TWO("2"),
        THREE("3");


        private final String thing;

        TestEnum(String thing) {
            this.thing = thing;
        }

        public String getThing() {
            return thing;
        }
    }

    public static final class EnumTemplate implements ConfigTemplate {
        @Value("test")
        public List<TestEnum> test;
    }
}
