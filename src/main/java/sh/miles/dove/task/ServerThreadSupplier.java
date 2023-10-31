package sh.miles.suketto.bukkit.task.ticking;

/**
 * Intended to be used within the {@link ServerThreadWorker}. Basic supplier of a value and can easily be extended
 *
 * @param <R> the result type
 */
public interface ServerThreadSupplier<R> extends ServerThreadWorker {

    /**
     * The result of the computation
     *
     * @return the resulting object
     */
    R getResult();

}
