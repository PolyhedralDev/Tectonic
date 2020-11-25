package abstractconfig;

import com.dfsek.tectonic.ConfigTemplate;
import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.exception.ConfigException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class AbstractTest {
    @Test
    public void abs() throws ConfigException {
        InputStream one = AbstractTest.class.getResourceAsStream("/abstract/one.yml");
        InputStream two = AbstractTest.class.getResourceAsStream("/abstract/two.yml");
        InputStream three = AbstractTest.class.getResourceAsStream("/abstract/three.yml");
        AbstractConfigLoader loader = new AbstractConfigLoader();
        List<Template> templateList = loader.load(Arrays.asList(one, two, three), Template.class);
    }

    private static class Template implements ConfigTemplate {
        @Value("a")
        public String a;
        @Value("b")
        public String b;
        @Value("c")
        public String c;
    }
}
