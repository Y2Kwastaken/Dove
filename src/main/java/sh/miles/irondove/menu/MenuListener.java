package sh.miles.irondove.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

class MenuListener implements Listener {

    private final MenuManager menuManager;

    MenuListener(@NotNull final MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @EventHandler
    public void onClick(@NotNull final InventoryClickEvent event) {
        menuManager.getMenu(event.getInventory()).ifPresent(menu -> menu.handleClick(event));
    }

    @EventHandler
    public void onClose(@NotNull final InventoryCloseEvent event) {
        menuManager.getMenu(event.getInventory()).ifPresent(menu -> menu.handleClose(event));
    }
}
