package sh.miles.dove.collection.registry;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Represents a registry that can not be registered too after creation
 *
 * @param <T> the type of the object
 */
public class FrozenRegistry<T extends RegistryKey> extends AbstractRegistry<T> {

    public FrozenRegistry(final Supplier<Map<String, T>> registrySupplier) {
        super(() -> ImmutableMap.copyOf(registrySupplier.get()));
    }
}
