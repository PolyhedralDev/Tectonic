import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.toml.TomlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TomlTest {
    @Test
    void toml() {
        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(Server.class, Server::new);
        TomlTemplate template = new TomlTemplate();
        loader.load(template, new TomlConfiguration(TomlTest.class.getResourceAsStream("/test.toml")));

        System.out.println("title: " + template.title);
        System.out.println("owner: " + template.ownerName);
        System.out.println("servers:");
        template.servers.forEach((name, server) -> System.out.println("  - " + name + ": [" + server.ip + ", " + server.role + "]"));
    }

    private static final class TomlTemplate implements ConfigTemplate {
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
