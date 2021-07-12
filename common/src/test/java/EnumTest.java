import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.YamlConfiguration;
import com.dfsek.tectonic.loading.ConfigLoader;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EnumTest {
    @Test
    public void testEnums() {
        EnumTemplate enumTemplate = new EnumTemplate();
        new ConfigLoader().load(enumTemplate, new YamlConfiguration("test: [TWO]"));
        System.out.println(enumTemplate.test.get(0).getThing());
    }

    public static final class EnumTemplate implements ConfigTemplate  {
        @Value("test")
        public List<TestEnum> test;
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
}
