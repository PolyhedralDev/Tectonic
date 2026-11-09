import com.dfsek.tectonic.toml.TomlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EscapedKeyTest {
    @Test
    void escapedDotReadsLiteralKey() {
        TomlConfiguration config = new TomlConfiguration("[x]\n\"y.z\" = 5\n\n[x.y]\nz = 6\n");
        assertEquals(5L, config.get("x.y\\.z"));
        assertEquals(6L, config.get("x.y.z"));
        assertTrue(config.contains("x.y\\.z"));
        assertTrue(config.contains("x.y.z"));
        assertFalse(config.contains("x.w\\.z"));
    }
}
