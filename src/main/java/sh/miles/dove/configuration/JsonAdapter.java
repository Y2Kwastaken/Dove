package sh.miles.dove.configuration;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * An adapter for to and from json operations
 *
 * @param <T> the type
 */
public interface JsonAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    /**
     * Retrieves the type of the adapter
     *
     * @return the type of the adapter
     */
    Class<T> getAdapterType();

    default boolean isHierarchy() {
        return false;
    }
}
