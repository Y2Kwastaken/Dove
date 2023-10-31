package sh.miles.dove.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MenuManager {

    private static MenuManager instance;

    private final Map<Inventory, Menu<?>> menus;

    private MenuManager() {
        this.menus = new HashMap<>();
    }

    public void register(@NotNull final Menu<?> menu) {
        menus.put(menu.getScene().getBukkitView().getTopInventory(), menu);
    }

    public void unregister(@NotNull final Inventory inventory) {
        menus.remove(inventory);
    }

    public Optional<Menu<?>> getMenu(@NotNull final Inventory inventory) {
        return Optional.ofNullable(menus.get(inventory));
    }

    public static void init(@NotNull final Plugin plugin) {
        instance = new MenuManager();
        Bukkit.getPluginManager().registerEvents(new MenuListener(instance), plugin);
    }

    public static MenuManager getInstance() {
        return instance;
    }

}
