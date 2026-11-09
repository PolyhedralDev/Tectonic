import com.dfsek.tectonic.HoconConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EscapedKeyTest {
    @Test
    void escapedDotReadsLiteralKey() {
        HoconConfiguration config = new HoconConfiguration("x { \"y.z\" = 5, y { z = 6 } }");
        assertEquals(5, ((Number) config.get("x.y\\.z")).intValue());
        assertEquals(6, ((Number) config.get("x.y.z")).intValue());
        assertTrue(config.contains("x.y\\.z"));
        assertFalse(config.contains("x.w\\.z"));
    }
}
