package sh.miles.dove.collection.registry;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An Updatable registry that can have contents added arbitrarily.
 *
 * @param <T> the type
 */
public class WriteableRegistry<T extends RegistryKey> extends AbstractRegistry<T> {

    public WriteableRegistry(final Supplier<Map<String, T>> registrySupplier) {
        super(registrySupplier);
    }

    public WriteableRegistry() {
        super(HashMap::new);
    }

    /**
     * Registers an object within the registry
     *
     * @param object the object to register
     * @return true if the value was successfully registered, otherwise false.
     */
    public boolean register(@NotNull final T object) {
        final T value = registry.get(object.getKey());
        if (value == null) {
            registry.put(object.getKey(), object);
            return true;
        }
        return false;
    }
}
