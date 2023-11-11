package sh.miles.dove.plugin;

import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Gets an economy plugin dependency
 *
 * @param <T> the type of plugin
 */
public interface EconomyPluginDependency<T extends Plugin> extends PluginDependency<T> {
    /**
     * Gets the balance of the player
     *
     * @param player the player to get the balance of
     * @return the balance
     */
    double getBalance(UUID player);

    /**
     * Withdraws the specified amount from the player
     *
     * @param player the player to withdraw from
     * @param amount the amount to withdraw
     * @return true if success, otherwise false
     * @throws UnsupportedOperationException if this was not implemented
     */
    boolean withdraw(UUID player, double amount) throws UnsupportedOperationException;

    /**
     * Deposits the specified amount from the player
     *
     * @param player the player to deposit from
     * @param amount the amount to deposit
     * @return true if success, otherwise false
     * @throws UnsupportedOperationException if this was not implemented
     */
    boolean deposit(UUID player, double amount);
}
