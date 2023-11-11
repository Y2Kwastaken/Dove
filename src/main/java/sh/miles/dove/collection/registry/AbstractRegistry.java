package sh.miles.dove.collection.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A basic abstract implementation of Registry
 *
 * @param <T> the type
 */
public abstract class AbstractRegistry<T extends RegistryKey> implements Registry<T> {

    protected final Map<String, T> registry;

    protected AbstractRegistry(Supplier<Map<String, T>> registrySupplier) {
        this.registry = registrySupplier.get();
    }

    @Override
    public Optional<T> get(@NotNull final String key) {
        return Optional.ofNullable(registry.get(key));
    }

    @Nullable
    @Override
    public T getOrNull(@NotNull final String key) {
        return registry.get(key);
    }

    @Override
    public Set<String> keys() {
        return registry.keySet();
    }
}
