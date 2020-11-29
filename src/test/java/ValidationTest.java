import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.ConfigLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ValidationTest {
    @Test
    public void validate() throws ConfigException {
        ConfigLoader loader = new ConfigLoader();
        Validate example = new Validate();
        loader.load(example, this.getClass().getResourceAsStream("/validate/valid.yml")); // This should validate

        try {
            loader.load(example, this.getClass().getResourceAsStream("/validate/invalid.yml")); // This should NOT validate
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
