package sh.miles.dove.inventory.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record MenuItem(@NotNull Function<Player, ItemStack> item, BiConsumer<Player, InventoryClickEvent> click) {

    public ItemStack item(@NotNull final Player player) {
        return item.apply(player);
    }

    public void click(@NotNull final Player player, @NotNull final InventoryClickEvent event) {
        click.accept(player, event);
    }

}
