package sh.miles.dove.lang;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.miles.dove.lang.replacement.Replacement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Creates a language file that can be read from
 */
public final class LanguageFile {

    public static final String STANDARD_EXTENSION = ".lang";

    private final Map<String, LanguageComponent> components;

    private LanguageFile(final Map<String, LanguageComponent> components) {
        this.components = components;
    }

    public LanguageComponent getText(final String key) {
        return components.get(key);
    }

    public Set<String> getKeys() {
        return components.keySet();
    }

    public void send(CommandSender sender, String key, Replacement... replacements) {
        final LanguageComponent component = getText(key);
        final List<BaseComponent[]> components;
        if (component.isList()) {
            components = component.getList(replacements);
        } else {
            components = new ArrayList<>(Collections.singleton(component.get(replacements)));
        }

        for (final BaseComponent[] baseComponents : components) {
            sender.spigot().sendMessage(baseComponents);
        }
    }

    public void send(CommandSender sender, String key) {
        final LanguageComponent component = getText(key);
        final List<BaseComponent[]> components;
        if (component.isList()) {
            components = component.getList();
        } else {
            components = new ArrayList<>(Collections.singleton(component.get()));
        }

        for (final BaseComponent[] baseComponents : components) {
            sender.spigot().sendMessage(baseComponents);
        }
    }

    /**
     * Creates a language file that can be read from
     *
     * @param fileName the name of the language file
     * @param folder   the folder it can be read from
     * @return the language file
     */
    public static LanguageFile create(final String fileName, @NonNull final Path folder) {
        Preconditions.checkArgument(Files.isDirectory(folder), "The provided path must be to a folder");
        Preconditions.checkArgument(fileName.endsWith(STANDARD_EXTENSION), "The provided fileName must end with the extension %s".formatted(STANDARD_EXTENSION));
        final Path filePath = folder.resolve(fileName);
        try {
            return new LanguageFile(LanguageFileParser.parse(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
