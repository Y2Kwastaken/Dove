package sh.miles.dove;

import com.google.gson.GsonBuilder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.dove.configuration.JsonAdapter;
import sh.miles.dove.configuration.JsonHelper;
import sh.miles.dove.plugin.DependencyRegistry;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Dove {

    private static Plugin plugin;
    private static DependencyRegistry dependencyRegistry;
    private static JsonHelper jsonHelper;

    public static void init(@NotNull final Plugin plugin, List<JsonAdapter<?>> adapters) {
        Dove.plugin = plugin;
        Dove.dependencyRegistry = new DependencyRegistry(plugin);
        Dove.jsonHelper = new JsonHelper(adapters);
    }

    public static DependencyRegistry getDependencyRegistry() {
        return dependencyRegistry;
    }

    public static JsonHelper getJsonHelper() {
        return jsonHelper;
    }

    @NotNull
    public static Logger log() {
        return plugin.getLogger();
    }
}
