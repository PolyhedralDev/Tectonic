package abstractconfig;

import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AbstractTest {
    @Test
    public void abs() throws ConfigException {
        InputStream one = AbstractTest.class.getResourceAsStream("/abstract/one.yml");
        InputStream two = AbstractTest.class.getResourceAsStream("/abstract/two.yml");
        InputStream three = AbstractTest.class.getResourceAsStream("/abstract/three.yml");
        InputStream abs = AbstractTest.class.getResourceAsStream("/abstract/abstract.yml");
        InputStream abs2 = AbstractTest.class.getResourceAsStream("/abstract/multi_base.yml");
        InputStream diamond = AbstractTest.class.getResourceAsStream("/abstract/diamond.yml");
        AbstractConfigLoader loader = new AbstractConfigLoader();
        System.out.println("building...");
        Set<Template> templateList = loader.loadTemplates(Arrays.asList(one, two, three, abs, abs2, diamond).stream().map(YamlConfiguration::new).collect(Collectors.toList()), Template::new);
        System.out.println("built...");

        for(Template t : templateList) {
            System.out.println(t.id + ":");
            System.out.println(t.a);
            System.out.println(t.b);
            System.out.println(t.c);
            System.out.println();
        }
    }

    private static class Template implements ConfigTemplate {

        @Value("id")
        @Final
        public String id;

        @Value("a")
        public String a;
        @Value("b")
        public String b;
        @Value("c")
        public String c;
    }
}
