package abstractconfig;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Merge;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MergeTest {
    @Test
    public void testMerge() throws ConfigException {
        InputStream one = AbstractTest.class.getResourceAsStream("/abstract/one.yml");
        InputStream two = AbstractTest.class.getResourceAsStream("/abstract/two.yml");
        InputStream three = AbstractTest.class.getResourceAsStream("/abstract/three.yml");
        InputStream abs = AbstractTest.class.getResourceAsStream("/abstract/abstract.yml");
        AbstractConfigLoader loader = new AbstractConfigLoader();
        List<Template> templateList = loader.load(Arrays.asList(one, two, three, abs), Template::new);

        for(Template t : templateList) {
            System.out.println(t.getId() + ": " + t.getMap());
            System.out.println();
        }
    }

    private static class Template implements ConfigTemplate {
        @Value("map")
        @Abstractable
        @Merge(Merge.Type.ALWAYS)
        private Map<String, String> map;

        @Value("id")
        private String id;

        public String getId() {
            return id;
        }

        public Map<String, String> getMap() {
            return map;
        }
    }
}
