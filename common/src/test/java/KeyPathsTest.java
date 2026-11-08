import com.dfsek.tectonic.api.config.KeyPaths;
import com.dfsek.tectonic.impl.MapConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class KeyPathsTest {
    @Test
    void splitPlain() {
        assertEquals(List.of("a", "b", "c"), KeyPaths.split("a.b.c"));
        assertEquals(List.of("a"), KeyPaths.split("a"));
    }

    @Test
    void splitEscapedDot() {
        assertEquals(List.of("a", "b.c"), KeyPaths.split("a.b\\.c"));
    }

    @Test
    void splitEscapedBackslash() {
        assertEquals(List.of("a\\", "b"), KeyPaths.split("a\\\\.b"));
    }

    @Test
    void splitInvalidEscapeThrowsNamingPath() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> KeyPaths.split("a.b\\x"));
        assertTrue(e.getMessage().contains("a.b\\x"));
        assertThrows(IllegalArgumentException.class, () -> KeyPaths.split("a\\"));
    }

    @Test
    void splitMatchesLegacyBehaviourForUnescapedPaths() {
        for(String path : new String[]{ "", ".", "a.", "a..b", ".a", "a.b.", "a/b.c-d" }) {
            assertEquals(List.of(path.split("\\.")), KeyPaths.split(path), "path: " + path);
        }
    }

    @Test
    void escapeRoundTrips() {
        assertEquals("a\\.b", KeyPaths.escape("a.b"));
        assertEquals("a\\\\b", KeyPaths.escape("a\\b"));
        for(String s : new String[]{ "plain", "a.b", "a\\b", "\\.", "..", "a\\.b\\\\", "minecraft/x.y" }) {
            assertEquals(List.of(s), KeyPaths.split(KeyPaths.escape(s)), "segment: " + s);
        }
    }

    @Test
    void mapConfigurationEscapedLookup() {
        MapConfiguration config = new MapConfiguration(Map.of("x", Map.of("y.z", 5, "y", Map.of("z", 6))));
        assertEquals(5, config.get("x.y\\.z"));
        assertEquals(6, config.get("x.y.z"));
        assertTrue(config.contains("x.y\\.z"));
        assertFalse(config.contains("x.q\\.z"));
    }
}
