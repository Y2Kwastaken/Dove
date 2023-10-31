package sh.miles.suketto.core.function;

/**
 * A supplier that can throw a checked exception
 *
 * @param <R> the type to be supplied
 */
@FunctionalInterface
public interface ThrowingSupplier<R> {

    /**
     * Gets the return type by applying the code inside the get method
     *
     * @return the return type
     * @throws Exception the checked exception thrown
     */
    R get() throws Exception;
}
