import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EscapedKeyTest {
    @Test
    void escapedDotReadsLiteralKey() {
        YamlConfiguration config = new YamlConfiguration("x:\n  y.z: 5\n  y:\n    z: 6\n");
        assertEquals(5, config.get("x.y\\.z"));
        assertEquals(6, config.get("x.y.z"));
        assertTrue(config.contains("x.y\\.z"));
        assertFalse(config.contains("x.w\\.z"));
    }
}
