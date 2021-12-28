import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

public class ExtensionTest {
    @Test
    public void load() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(Type.class, (TypeLoader<Type>) (t, c, loader1, depthTracker) -> Type.valueOf((String) c));
        Configuration c = new YamlConfiguration(ExtensionTest.class.getResourceAsStream("/generic/one.yml"));

        Chooser chooser = new Chooser();
        loader.load(chooser, c);
        System.out.println(chooser.getType());

        ConfigTemplate template = chooser.getType().getConfig();

        loader.load(template, c);

        if(template instanceof OneConfig) {
            System.out.println(((OneConfig) template).getString());
        }
        if(template instanceof TwoConfig) {
            System.out.println(((TwoConfig) template).getInteger());
        }
        if(template instanceof ThreeConfig) {
            System.out.println(((ThreeConfig) template).getDouble());
        }
    }

    public enum Type {
        ONE {
            @Override
            public ConfigTemplate getConfig() {
                return new OneConfig();
            }
        }, TWO {
            @Override
            public ConfigTemplate getConfig() {
                return new TwoConfig();
            }
        }, THREE {
            @Override
            public ConfigTemplate getConfig() {
                return new ThreeConfig();
            }
        };

        public abstract ConfigTemplate getConfig();
    }

    public static class Chooser implements ConfigTemplate {
        @Value("type")
        private Type type;

        public Type getType() {
            return type;
        }
    }

    public static class OneConfig implements ConfigTemplate {
        @Value("string")
        private String string;

        public String getString() {
            return string;
        }
    }

    public static class TwoConfig implements ConfigTemplate {
        @Value("int")
        private int integer;

        public int getInteger() {
            return integer;
        }
    }

    public static class ThreeConfig implements ConfigTemplate {
        @Value("double")
        private double aDouble;

        public double getDouble() {
            return aDouble;
        }
    }
}
