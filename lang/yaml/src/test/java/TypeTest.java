import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;

public class TypeTest {

    @Test
    public void test() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(ExampleInterface.class, new ExampleLoader());

        Config config = new Config();

        loader.load(config, new YamlConfiguration(TypeTest.class.getResourceAsStream("/test.yml")));

        for(ExampleInterface i : config.getNumbers()) {
            System.out.println(i.getNumber());
        }

    }


    // Class examples
    public interface ExampleInterface {
        int getNumber();
    }

    public static class Config implements ConfigTemplate {
        @Value("numbers")
        private List<ExampleInterface> numbers;

        public List<ExampleInterface> getNumbers() {
            return numbers;
        }
    }

    public static class One implements ExampleInterface {
        private final String a;

        public One(String a) {
            this.a = a;
        }

        public String getA() {
            return a;
        }

        @Override
        public int getNumber() {
            return 1;
        }
    }

    public static class Two implements ExampleInterface {
        private final int a;

        public Two(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }

        @Override
        public int getNumber() {
            return 2;
        }
    }

    public static class Three implements ExampleInterface {
        private final double a;

        public Three(double a) {
            this.a = a;
        }

        public double getA() {
            return a;
        }

        @Override
        public int getNumber() {
            return 3;
        }
    }

    // Type loader

    @SuppressWarnings("unchecked")
    public static class ExampleLoader implements TypeLoader<ExampleInterface> {

        @Override
        public ExampleInterface load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) throws LoadException {
            System.out.println(c.getClass());
            Map<String, Object> map = (Map<String, Object>) c;
            switch((String) map.get("type")) {
                case "one":
                    return new One((String) map.get("string"));
                case "two":
                    return new Two((Integer) map.get("int"));
                case "three":
                    return new Three((Double) map.get("double"));
            }
            return null;
        }
    }
}
