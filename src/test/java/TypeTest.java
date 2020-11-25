import com.dfsek.polyconfig.ConfigTemplate;
import com.dfsek.polyconfig.annotations.Value;
import com.dfsek.polyconfig.exception.ConfigException;
import com.dfsek.polyconfig.exception.LoadException;
import com.dfsek.polyconfig.loading.ConfigLoader;
import com.dfsek.polyconfig.loading.TypeLoader;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TypeTest {

    @Test
    public void test() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(ExampleInterface.class, new ExampleLoader());

        Config config = new Config();

        loader.load(config, TypeTest.class.getResourceAsStream("/test.yml"));

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
        public ExampleInterface load(Type t, Object c, ConfigLoader loader) throws LoadException {
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
