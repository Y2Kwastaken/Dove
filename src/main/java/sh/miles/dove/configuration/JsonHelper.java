package sh.miles.dove.configuration;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.dove.configuration.adapter.inventory.ItemStackAdapter;
import sh.miles.dove.configuration.adapter.world.BlockDataJsonAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * A utility to help with Json read and write operations
 */
public class JsonHelper {

    private final Gson gson;

    public JsonHelper(Consumer<GsonBuilder> build) {
        final GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        build.accept(builder);
        registerDefaults(builder);
        this.gson = builder.create();
    }

    public JsonHelper(List<JsonAdapter<?>> adapters) {
        final GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        for (final JsonAdapter<?> adapter : adapters) {
            if (adapter.isHierarchy()) {
                builder.registerTypeHierarchyAdapter(adapter.getAdapterType(), adapter);
            } else {
                builder.registerTypeAdapter(adapter.getAdapterType(), adapter);
            }
        }
        registerDefaults(builder);
        this.gson = builder.create();
    }

    public JsonHelper(JsonAdapter<?>... adapters) {
        this(Arrays.asList(adapters));
    }

    @NotNull
    public <T> T[] asArray(@NotNull final Plugin plugin, @NotNull String file, Class<T[]> arrayClazz) {
        Preconditions.checkArgument(arrayClazz.isArray(), "An array class must be passed");
        try {
            return gson.fromJson(new FileReader(new File(plugin.getDataFolder(), file)), arrayClazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    public Gson getGson() {
        return gson;
    }

    private static void registerDefaults(@NotNull final GsonBuilder builder) {
        builder.registerTypeHierarchyAdapter(BlockData.class, new BlockDataJsonAdapter());
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
    }

}
