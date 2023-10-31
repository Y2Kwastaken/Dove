package sh.miles.suketto.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.suketto.core.utils.ReflectionUtils;

import java.util.Map;

/**
 * Holds official interactions between SpigotAPI commandMap and Suketto Command classes
 */
public final class CommandRegistrar {

    private final CommandMap commandMap;
    private final Map<String, org.bukkit.command.Command> knownCommands;

    /**
     * Creates SCommandRegistrar
     */
    @SuppressWarnings("unchecked")
    public CommandRegistrar() {
        this.commandMap = ReflectionUtils.getField(Bukkit.getPluginManager(), "commandMap", CommandMap.class);
        this.knownCommands = (Map<String, org.bukkit.command.Command>) ReflectionUtils.getField(commandMap, "knownCommands", Map.class);
    }

    /**
     * Registers a command to the server by using spigot's internal {@link PluginCommand} class
     *
     * @param plugin  the plugin to registere the command with
     * @param command the command to register
     */
    public void register(@NotNull final Plugin plugin, @NotNull final Command command) {
        final CommandLabel label = command.getCommandLabel();
        final PluginCommand pluginCommand = ReflectionUtils.newInstance(PluginCommand.class, new Object[]{label.getName(), plugin});
        if (pluginCommand == null) {
            throw new RuntimeException("Creation of PluginCommand failed");
        }
        pluginCommand.setName(label.getName());
        pluginCommand.setAliases(label.getAliases());
        pluginCommand.setPermission(label.getPermission());
        pluginCommand.setUsage("/" + label.getName());
        pluginCommand.setExecutor((s, c, l, a) -> command.execute(s, a));
        pluginCommand.setTabCompleter((s, c, l, a) -> command.complete(s, a));

        if (!commandMap.register(plugin.getName(), pluginCommand)) {
            throw new IllegalStateException("Command with the name " + pluginCommand.getName() + " already exists");
        }
    }

    /**
     * Unregisters all commands associated with the provided plugin
     *
     * @param plugin the plugins whose commands to unregister
     */
    public void unregisterAll(@NotNull final Plugin plugin) {
        knownCommands.entrySet().removeIf((Map.Entry<String, org.bukkit.command.Command> entry) -> {
            final String label = entry.getKey();
            if (!(entry.getValue() instanceof PluginCommand pluginCommand)) {
                return false;
            }

            return plugin.getName().equals(pluginCommand.getPlugin().getName());
        });
    }

}
