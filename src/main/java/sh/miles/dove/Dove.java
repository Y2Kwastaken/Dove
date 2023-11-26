package sh.miles.dove;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.dove.configuration.JsonAdapter;
import sh.miles.dove.configuration.JsonHelper;
import sh.miles.dove.plugin.DependencyRegistry;
import sh.miles.dove.task.ServerThreadTicker;

import java.util.List;
import java.util.logging.Logger;

public class Dove {

    private static Plugin plugin;
    private static DependencyRegistry dependencyRegistry;
    private static JsonHelper jsonHelper;
    private static ServerThreadTicker serverTicker;

    public static void init(@NotNull final Plugin plugin, List<JsonAdapter<?>> adapters) {
        Dove.plugin = plugin;
        Dove.dependencyRegistry = new DependencyRegistry(plugin);
        Dove.jsonHelper = new JsonHelper(adapters);
        Dove.serverTicker = new ServerThreadTicker();

        Bukkit.getScheduler().runTaskTimer(plugin, Dove.serverTicker, 1L, 1L);
    }

    public static DependencyRegistry getDependencyRegistry() {
        return dependencyRegistry;
    }

    public static JsonHelper getJsonHelper() {
        return jsonHelper;
    }

    public static ServerThreadTicker getServerTicker() {
        return serverTicker;
    }

    @NotNull
    public static Logger log() {
        return plugin.getLogger();
    }
}
