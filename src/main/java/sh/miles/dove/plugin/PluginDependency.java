package sh.miles.dove.plugin;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.dove.collection.registry.RegistryKey;

/**
 * outlines a basic soft dependency for a plugin
 *
 * @param <T> the type extending JavaPlugin
 */
public interface PluginDependency<T extends Plugin> extends RegistryKey {
    /**
     * Called when loading the dependency
     *
     * @param dependant the depending plugin
     */
    void load(@NotNull Plugin dependant);

    /**
     * Called when unloading the dependency
     *
     * @param dependant the depending plugin
     */
    void unload(@NotNull Plugin dependant);

    /**
     * @return true if is loaded, otherwise false.
     */
    boolean isLoaded();

    /**
     * @return the plugin if the dependency is loaded otherwise false
     */
    @Nullable
    T getPlugin();
}
