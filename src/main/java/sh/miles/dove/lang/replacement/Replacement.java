package sh.miles.suketto.bukkit.chat.translation.replacing;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Replacements are applied to specific replacers within a given Component. e.g. a component with the raw text "Your
 * value is {value}" must be replaced with "value" in order to be turned fully into a component. a corresponding
 * replacement would be the "value" key and the object you want to apply to it for example 5 or 0
 */
public final class Replacement extends Replacer {

    private final Object value;

    /**
     * @param key   the key the replacement targets
     * @param value the value that will be applied
     */
    public Replacement(@NotNull final String key, @NotNull final Object value) {
        super(key);
        this.value = value;
    }

    /**
     * Applies the given value at the specified key to the given string
     *
     * @param s the string to apply the replacement to
     * @return the newly modified string
     */
    public String[] apply(String... s) {
        Preconditions.checkNotNull(s);
        List<String> applied = new ArrayList<>();
        for (String string : s) {
            applied.add(apply(string, value));
        }
        return applied.toArray(String[]::new);
    }

    /**
     * The value of the replacement
     *
     * @return the object
     */
    public Object value() {
        return value;
    }

    @Override
    public String toString() {
        return "Replacement[" +
                "key=" + key() + ", " +
                "value=" + value + ']';
    }

    /**
     * Creates replacement
     *
     * @param key   key
     * @param value value
     * @return new replacement
     */
    public static Replacement of(@NotNull final String key, @NotNull final Object value) {
        return new Replacement(key, value);
    }

}
