package sh.miles.dove.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.dove.inventory.item.MenuItem;
import sh.miles.ironpipe.api.inventory.scene.ContainerScene;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Menu<T extends ContainerScene> {

    private final T scene;
    protected final Inventory inventory;
    private final Map<Integer, MenuItem> slots;
    protected ItemStack backdrop = new ItemStack(Material.AIR);

    protected Menu(@NotNull Function<Player, T> function, Player player) {
        this.scene = function.apply(player);
        this.inventory = scene.getBukkitView().getTopInventory();
        this.slots = new HashMap<>();
    }

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

    public void open() {
        decorate((Player) scene.getBukkitView().getPlayer());
        MenuManager.getInstance().register(this);
        getPlayer().openInventory(getScene().getBukkitView());
        final InventoryOpenEvent event = new InventoryOpenEvent(getScene().getBukkitView());
        Bukkit.getPluginManager().callEvent(event);
        handleOpen(event);
    }

    public void handleClick(@NotNull final InventoryClickEvent event) {
        final int slot = event.getSlot();
        getItem(slot).ifPresent(item -> item.click((Player) event.getWhoClicked(), event));
    }

    public void handleOpen(@NotNull final InventoryOpenEvent event) {
    }

    public void handleClose(@NotNull final InventoryCloseEvent event) {
    }
}
