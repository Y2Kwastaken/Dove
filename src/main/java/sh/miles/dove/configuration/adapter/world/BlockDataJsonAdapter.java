package sh.miles.dove.configuration.adapter.world;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import sh.miles.dove.configuration.JsonAdapter;

import java.lang.reflect.Type;

public class BlockDataJsonAdapter implements JsonAdapter<BlockData> {

    @Override
    public BlockData deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        final String data = element.getAsString();
        return Bukkit.getServer().createBlockData(data);
    }

    @Override
    public JsonElement serialize(final BlockData data, final Type type, final JsonSerializationContext context) {
        return new JsonPrimitive(data.getAsString());
    }

    @Override
    public Class<BlockData> getAdapterType() {
        return BlockData.class;
    }

    @Override
    public boolean isHierarchy() {
        return true;
    }
}
