package sh.miles.suketto.bukkit.task.ticking;

/**
 * Executed as a callback when a ServerThreadWorker completes
 *
 * @param <R> the result
 */
public interface ServerThreadCallback<R> {

    /**
     * Completes using the result given to the callback
     *
     * @param result the result
     */
    void complete(R result);
}
