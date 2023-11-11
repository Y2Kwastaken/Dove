package sh.miles.dove.plugin;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import sh.miles.dove.collection.registry.WriteableRegistry;

/**
 * Registry handles all Dependencies
 */
public final class DependencyRegistry extends WriteableRegistry<PluginDependency<?>> {

    private final Plugin plugin;

    public DependencyRegistry(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the dependency of the given class
     *
     * @param clazz the class
     * @param <T>   type
     * @return the dependency or null
     */
    @Nullable
    public <T extends PluginDependency<?>> T getRegistered(Class<T> clazz) {
        for (final PluginDependency<?> value : registry.values()) {
            if (value.getClass().isInstance(clazz)) {
                return clazz.cast(value);
            }
        }
        return null;
    }

    /**
     * Gets first enabled dependency of the given class
     *
     * @param clazz the clazz
     * @param <T>   the type to get
     * @return the first enabled dependency or null
     */
    public <T extends PluginDependency<?>> T getEnabled(Class<T> clazz) {
        for (final PluginDependency<?> value : registry.values()) {
            if (value.getClass().isInstance(clazz) && value.isLoaded()) {
                return clazz.cast(value);
            }
        }
        return null;
    }

    /**
     * Loads all dependencies
     */
    public void loadAll() {
        for (final PluginDependency<?> value : registry.values()) {
            value.load(plugin);
        }
    }

    /**
     * Unloads all dependencies
     */
    public void unloadAll() {
        for (final PluginDependency<?> value : registry.values()) {
            value.unload(plugin);
        }
    }
}
