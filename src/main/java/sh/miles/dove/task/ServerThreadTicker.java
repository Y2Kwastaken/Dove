package sh.miles.suketto.bukkit.task.ticking;

import org.jetbrains.annotations.NotNull;
import sh.miles.suketto.core.collection.abstracts.Tuple;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * injects tasks into the main thread. The ServerThreadTicker will attempt to only run as many tasks as possible on the
 * main thread without heavily impacting main thread performance
 */
public class ServerThreadTicker implements Runnable {

    /**
     * MAX MILLIS PER TICK
     */
    public static final double MAX_MILLIS_PER_TICK = 2.5;
    /**
     * MAX NANOS PER TICK
     */
    public static final int MAX_NANOS_PER_TICK = (int) (MAX_MILLIS_PER_TICK * 1E6);

    private final Deque<Tuple<ServerThreadWorker, ServerThreadCallback<Object>>> workers = new ConcurrentLinkedDeque<>();

    /**
     * Queues a task on the main thread
     *
     * @param worker the worker to queue
     */
    public void queue(@NotNull final ServerThreadWorker worker) {
        this.workers.add(Tuple.of(worker, null));
    }

    /**
     * Queues a task on the main thread with a callback
     *
     * @param worker   the worker to queue
     * @param callback the callback to execute when the worker finished
     */
    public void queue(@NotNull final ServerThreadWorker worker, @NotNull final ServerThreadCallback<Object> callback) {
        this.workers.add(Tuple.of(worker, callback));
    }

    @Override
    public void run() {
        long stopTime = System.nanoTime() + MAX_NANOS_PER_TICK;

        Tuple<ServerThreadWorker, ServerThreadCallback<Object>> next;
        ServerThreadWorker worker;
        ServerThreadCallback<Object> callback;
        while (System.nanoTime() <= stopTime && (next = this.workers.poll()) != null) {
            worker = next.getFirst();
            callback = next.getSecond();
            try {
                worker.compute();
                Object value = null;
                if (worker instanceof ServerThreadSupplier<?>) {
                    value = ((ServerThreadSupplier<?>) worker).getResult();
                }
                callback.complete(value);
            } catch (Throwable throwable) {
                worker.exceptionally(throwable);
            }
        }
    }
}
