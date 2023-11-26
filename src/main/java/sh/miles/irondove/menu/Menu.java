package sh.miles.irondove.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.irondove.menu.item.MenuItem;
import sh.miles.ironpipe.api.annotations.PipeRequired;
import sh.miles.ironpipe.api.inventory.scene.ContainerScene;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a simple Menu class which displays a menu to the player
 *
 * @param <T> the scene to show to the player
 */
@PipeRequired
public class Menu<T extends ContainerScene> {

    private final T scene;
    protected final Inventory inventory;
    private final Map<Integer, MenuItem> slots;
    protected ItemStack backdrop = new ItemStack(Material.AIR);

    /**
     * Initializes the Menu class
     *
     * @param function the function that provides the scene to the menu
     * @param player   the player which the menu will be displayed too
     */
    protected Menu(@NotNull Function<Player, T> function, Player player) {
        this.scene = function.apply(player);
        this.inventory = scene.getBukkitView().getTopInventory();
        this.slots = new HashMap<>();
    }

    /**
     * Decorates the menu for the viewer. Should generally be inherited by child to contain decoration logic.
     *
     * @param viewer the viewer of the menu
     */
    protected void decorate(@NotNull final Player viewer) {
        slots.forEach((slot, item) -> inventory.setItem(slot, item.item(viewer)));
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, backdrop);
                continue;
            }

            if (inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, backdrop);
            }
        }
    }

    protected final void setItem(int slot, MenuItem item) {
        slots.put(slot, item);
    }

    @NotNull
    public Optional<MenuItem> getItem(int slot) {
        return Optional.ofNullable(this.slots.get(slot));
    }

    @NotNull
    public T getScene() {
        return this.scene;
    }

    @NotNull
    public Player getPlayer() {
        return (Player) this.scene.getBukkitView().getPlayer();
    }

    public void open(@NotNull final MenuManager menuManager) {
        decorate((Player) scene.getBukkitView().getPlayer());
        getPlayer().openInventory(getScene().getBukkitView());
        final InventoryOpenEvent event = new InventoryOpenEvent(getScene().getBukkitView());
        Bukkit.getPluginManager().callEvent(event);
        handleOpen(event);
    }

    public void handleClick(@NotNull final InventoryClickEvent event) {
        event.setCancelled(true);
        final int slot = event.getSlot();
        getItem(slot).ifPresent(item -> item.click((Player) event.getWhoClicked(), event));
    }

    public void handleOpen(@NotNull final InventoryOpenEvent event) {
    }

    public void handleClose(@NotNull final InventoryCloseEvent event) {
    }
}
