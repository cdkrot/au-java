package me.cdkrot.threads;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents future result from a thread pool.
 * @param <R> result type.
 */
public class LightFuture<R> {
    private ThreadPool pool;
    private Supplier<R> supplier;
    private boolean computed = false;
    private R result;
    private Exception caught;
    
    protected LightFuture(ThreadPool pool, Supplier<R> supplier) {
        this.pool = pool;
        this.supplier = supplier;
    }

    /**
     * Checks if the value is allready computed
     * @return is the value computed.
     */
    public boolean isReady() {
        synchronized (this) {
            return computed;
        }
    }

    /**
     * Returns computed value, waits if necessary.
     * @return computed result
     * @throws LightException if async computation has failed with exception.
     */
    public R get() {
        synchronized (this) {
            while (!computed)
                try {
                    this.wait();
                } catch (Exception ex) {}
        }

        if (caught != null)
            throw new LightException(caught);
        return result;
    }

    /**
     * Returns a new future corresponding to this future result transformed by function
     * @param <T> type of the new future
     * @param f function to call
     * @return new future associated with the same thread pool as this one.
     */
    public <T> LightFuture<T> thenApply(Function<R, T> f) {
        return pool.add(() -> f.apply(get()));
    }
    
    protected void compute() {
        try {
            result = supplier.get();
        } catch (Exception ex) {
            caught = ex;
        }
        
        synchronized (this) {
            computed = true;
            this.notifyAll();
        }
    }
}
