import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.exception.ValidationException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ValidationTest {
    @Test
    public void validate() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        Validate example = new Validate();
        loader.load(example, new YamlConfiguration(this.getClass().getResourceAsStream("/validate/valid.yml"))); // This should validate

        try {
            loader.load(example, new YamlConfiguration(this.getClass().getResourceAsStream("/validate/invalid.yml"))); // This should NOT validate
            fail();
        } catch(ValidationException ignored) {

        }
    }

    @SuppressWarnings("unused")
    private static class Validate implements ValidatedConfigTemplate {

        @Value("value1")
        private String value1;

        @Value("value2")
        private String value2;

        @Override
        public boolean validate() {
            return value1.equals("OK") && value2.equals("fsdfdsaf");
        }
    }
}
