package sh.miles.dove.lang;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import sh.miles.dove.AbstractPluginTest;
import sh.miles.dove.configuration.JsonHelper;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonHelperTest extends AbstractPluginTest {

    private Gson gson;

    @Override
    @BeforeEach
    public void setup() {
        super.setup();
        JsonHelper helper = assertDoesNotThrow(() -> new JsonHelper(new ArrayList<>()));
        gson = helper.getGson();
    }

}
