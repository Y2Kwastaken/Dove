package sh.miles.suketto.bukkit.task.ticking;

import org.bukkit.Bukkit;

/**
 * Intended to be used within the {@link ServerThreadWorker}. Basic computation value and can easily be extended
 */
public interface ServerThreadWorker {

    /**
     * Does the necessary computation
     */
    void compute() throws Throwable;

    /**
     * Run when an exception occurs and output is needed
     */
    default void exceptionally(Throwable throwable) {
        Bukkit.getLogger().throwing("ServerThreadWorker", "exceptionally", throwable);
    }

}
