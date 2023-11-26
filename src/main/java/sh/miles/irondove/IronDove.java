package sh.miles.irondove;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.irondove.menu.MenuManager;
import sh.miles.ironpipe.api.annotations.PipeRequired;

/**
 * An Adapter class which initializes all IronPipe Features required within dove All features that require IronPipe are
 * also accordingly stored within the sh.miles.irondove directory.
 */
@PipeRequired
public final class IronDove {

    private static Plugin plugin;
    private static MenuManager menuManager;

    public static void initialize(@NotNull final Plugin plugin) {
        IronDove.plugin = plugin;
        IronDove.menuManager = new MenuManager(plugin);
    }

    public static MenuManager getMenuManager() {
        return menuManager;
    }
}
