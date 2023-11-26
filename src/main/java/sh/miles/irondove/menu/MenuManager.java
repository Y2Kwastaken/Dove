package sh.miles.irondove.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MenuManager {

    private final Map<Inventory, Menu<?>> menus;

    public MenuManager(@NotNull final Plugin plugin) {
        this.menus = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(new MenuListener(this), plugin);
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


}
