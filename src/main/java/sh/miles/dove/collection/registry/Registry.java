package sh.miles.dove.collection.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Represents a basic registry object which contained {@link RegistryKey} objects
 */
public interface Registry<T extends RegistryKey> {

    /**
     * Fetches an entry from the registry
     *
     * @param key the key to use to fetch the entry
     * @return an optional wrapping the nullable result
     */
    Optional<T> get(@NotNull final String key);

    /**
     * Fetches an entry from the registry
     *
     * @param key the key to use to fetch the entry
     * @return the resulting object at that key, or null if it doesn't exist
     */
    @Nullable
    T getOrNull(@NotNull final String key);

    /**
     * Retrieves a set of all keys from the registry
     *
     * @return a set of string keys
     */
    Set<String> keys();

    static <T extends RegistryKey> WriteableRegistry<T> newWriteable(Supplier<Map<String, T>> data) {
        return new WriteableRegistry<>(data);
    }

    static <T extends RegistryKey> FrozenRegistry<T> newFrozen(Supplier<Map<String, T>> data) {
        return new FrozenRegistry<>(data);
    }
}
