package sh.miles.suketto.bukkit.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import sh.miles.suketto.bukkit.chat.Component;

/**
 * Represents CommandSettings that can be applied to a command for enhanced feature sets
 */
public class CommandSettings {

    private static final BaseComponent DEFAULT_PERMISSION_MESSAGE = Component.unarray(TextComponent.fromLegacyText("You do not have permission for this", ChatColor.RED));
    private static final BaseComponent DEFAULT_INVALID_SENDER_MESSAGE = Component.unarray(TextComponent.fromLegacyText("You are not a valid sender for this command", ChatColor.RED));
    public static Settings DEFAULT_COMMAND_SETTINGS = new Settings(DEFAULT_PERMISSION_MESSAGE, DEFAULT_INVALID_SENDER_MESSAGE);

    private BaseComponent permissionMessage;
    private BaseComponent invalidSenderMessage;

    /**
     * Sets the permission message
     *
     * @param permissionMessage the message to set
     */
    public void setPermissionMessage(BaseComponent permissionMessage) {
        this.permissionMessage = permissionMessage.duplicate();
    }

    /**
     * Sets the invalid sender message
     *
     * @param invalidSenderMessage the invalid sender message to set
     */
    public void setInvalidSenderMessage(BaseComponent invalidSenderMessage) {
        this.invalidSenderMessage = invalidSenderMessage.duplicate();
    }

    /**
     * Builds the CommandSettings instance into a immutable {@link Settings} record which can not be modified
     *
     * @return a Settings instance
     */
    public Settings build() {
        return new Settings(permissionMessage, invalidSenderMessage);
    }

    /**
     * Gets default permission
     *
     * @return the component
     */
    public static BaseComponent getDefaultPermissionMessage() {
        return DEFAULT_PERMISSION_MESSAGE.duplicate();
    }

    /**
     * Gets default invalid sender
     *
     * @return the base component
     */
    public static BaseComponent getDefaultInvalidSenderMessage() {
        return DEFAULT_INVALID_SENDER_MESSAGE.duplicate();
    }

    /**
     * A Build Settings Object comprised from the Settings Builder
     *
     * @param permissionMessage    the permission message
     * @param invalidSenderMessage the invalid sender message
     */
    public record Settings(BaseComponent permissionMessage, BaseComponent invalidSenderMessage) {

        /**
         * Sends the permission message
         *
         * @param sender the sender
         */
        public void sendPermissionMessage(CommandSender sender) {
            if (permissionMessage != null) {
                sender.spigot().sendMessage(permissionMessage);
            }
        }

        /**
         * sends the invalid sender message
         *
         * @param sender the sender
         */
        public void sendInvalidSenderMessage(CommandSender sender) {
            if (invalidSenderMessage != null) {
                sender.spigot().sendMessage(invalidSenderMessage);
            }
        }
    }

}
