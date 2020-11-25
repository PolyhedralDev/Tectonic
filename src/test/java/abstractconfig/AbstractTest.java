package abstractconfig;

import com.dfsek.tectonic.ConfigTemplate;
import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.annotations.Abstractable;
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
        System.out.println("building...");
        List<Template> templateList = loader.load(Arrays.asList(one, two, three), Template::new);
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
        public String id;
        @Value("a")
        @Abstractable
        public String a;
        @Value("b")
        @Abstractable
        public String b;
        @Value("c")
        @Abstractable
        public String c;
    }
}
