package sh.miles.suketto.bukkit.chat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.BaseComponent;

import java.lang.reflect.Field;

/**
 * ComponentSerializer
 */
public class ComponentSerializer extends net.md_5.bungee.chat.ComponentSerializer {

    private static final Gson gson;

    static {
        try {
            final Field gsonField = net.md_5.bungee.chat.ComponentSerializer.class.getDeclaredField("gson");
            gsonField.setAccessible(true);
            gson = (Gson) gsonField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserialize a JSON-compliant String as a single component. The input is expected to be a JSON object that
     * represents only one component.
     *
     * @param json the component json to parse
     * @return the deserialized component
     * @throws IllegalArgumentException if anything other than a JSON object is passed as input
     */
    public static BaseComponent deserialize(String json) {
        JsonElement jsonElement = JsonParser.parseString(json);
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("Malformed JSON. Expected object, got array for input \"" + json + "\".");
        }

        return gson.fromJson(jsonElement, BaseComponent.class);
    }

}
