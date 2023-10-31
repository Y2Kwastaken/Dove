package sh.miles.dove.lang.replacement;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import sh.miles.suketto.bukkit.chat.translation.TranslationComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Represents a replacement within a {@link TranslationComponent} without a yet known value. e.g. "Your value is
 * {value}" would have the replacement with the key "value".
 */
public class Replacer implements BiFunction<String, Object, String> {

    /**
     * Open char
     */
    public static final char REPLACE_KEY_START = '{';
    /**
     * end char
     */
    public static final char REPLACE_KEY_END = '}';
    private final String key;

    /**
     * @param key the key that must be replaced
     */
    public Replacer(@NotNull String key) {
        this.key = key;
    }


    /**
     * Checks whether thr provided string contains the replacer key
     *
     * @param s the string to check
     * @return true if the replacer is contained withing the provided string otherwise false
     */
    public boolean isContainedWithin(String s) {
        return s.contains(key);
    }

    /**
     * Applies the provided object in place of the key for the provided string
     *
     * @param s the provided string to replace
     * @param o the object to replace the key with
     * @return the new string
     */
    @Override
    public String apply(String s, Object o) {
        Preconditions.checkNotNull(s);
        Preconditions.checkNotNull(o);
        return s.replace(REPLACE_KEY_START + key + REPLACE_KEY_END, o.toString());
    }

    /**
     * The key to create
     *
     * @return the string
     */
    @NotNull
    public String key() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Replacer that)) return false;
        return Objects.equals(this.key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Replacer[" +
                "key=" + key + ']';
    }

    /**
     * Creates replacer from the key
     *
     * @param key the key
     * @return the new replacer
     */
    public static Replacer of(@NotNull final String key) {
        return new Replacer(key);
    }

    public static Replacer[] inStrings(@NotNull final String... strings) {
        final List<Replacer> replacers = new ArrayList<>();
        for (String string : strings) {
            replacers.addAll(inString(string));
        }
        return replacers.toArray(Replacer[]::new);
    }

    private static List<Replacer> inString(@NotNull final String string) {
        final List<Replacer> replacers = new ArrayList<>();
        final StringBuilder builder = new StringBuilder(string);
        int open;
        int close;
        while ((open = builder.indexOf(String.valueOf(REPLACE_KEY_START))) != -1 && (close = builder.indexOf(String.valueOf(REPLACE_KEY_END))) != -1) {
            replacers.add(of(builder.substring(open + 1, close)));
            builder.replace(open, close + 1, "");
        }
        return replacers;
    }
}
