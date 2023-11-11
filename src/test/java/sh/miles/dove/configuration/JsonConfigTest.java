package sh.miles.dove.configuration;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.miles.dove.AbstractPluginTest;

import static org.junit.jupiter.api.Assertions.*;

public class JsonConfigTest extends AbstractPluginTest {


    private Gson gson;
    private JsonConfig config;

    @Override
    @BeforeEach
    public void setup() {
        super.setup();
        plugin.saveResource("test-config.json", true);
        gson = new Gson();
        config = assertDoesNotThrow(() -> JsonConfig.readConfig(gson, plugin, "test-config.json"));

    }

    @Test
    public void testGet() {
        assertNotNull(config.getString("string-key"));
        assertNotNull(config.getInt("int-key"));
        assertNotNull(config.getDouble("double-key"));
        assertNotNull(config.getBoolean("boolean-key"));
        assertNotNull(config.getSection("custom-object"));
        assertNotNull(config.getObjectAndMap("custom-object", (object) ->
                new MockPerson(object.get("name").getAsString(), object.get("age").getAsInt())));
    }

    private static record MockPerson(String name, int age) {

    }
}
