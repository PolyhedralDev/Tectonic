import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.json.JsonConfiguration;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class JsonTest {
    @Test
    void json() {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(Server.class, Server::new);
        JsonTemplate template = new JsonTemplate();
        loader.load(template, new JsonConfiguration(JsonTest.class.getResourceAsStream("/test.json")));

        System.out.println("title: " + template.title);
        System.out.println("owner: " + template.ownerName);
        System.out.println("servers:");
        template.servers.forEach((name, server) -> System.out.println("  - " + name + ": [" + server.ip + ", " + server.role + "]"));
    }

    private static final class JsonTemplate implements ConfigTemplate {
        @Value("title")
        public String title;

        @Value("owner.name")
        public String ownerName;

        @Value("servers")
        public Map<String, Server> servers;
    }

    private static final class Server implements ObjectTemplate<Server> {
        @Value("ip")
        public String ip;
        @Value("role")
        public String role;

        @Override
        public Server get() {
            return this;
        }
    }
}
