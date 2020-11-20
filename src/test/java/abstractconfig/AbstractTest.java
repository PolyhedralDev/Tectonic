package abstractconfig;

import com.dfsek.polyconfig.ConfigTemplate;
import com.dfsek.polyconfig.abstraction.AbstractConfigLoader;
import com.dfsek.polyconfig.annotations.Value;
import com.dfsek.polyconfig.exception.ConfigException;
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
