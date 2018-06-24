package me.cdkrot.threads;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;


public class ThreadPool implements AutoCloseable {
    private Thread[] threads;
    private boolean terminated = false;
    private Queue<LightFuture<?>> queue = new ArrayDeque<LightFuture<?>>();
    
    /**
     * Creates new Thread Pool with specified number of threads.
     * @param k number of threads
     * @throws IllegalArgumentException if k below zero.
     */
    public ThreadPool(int k) {
        if (k <= 0)
            throw new IllegalArgumentException("Number of threads must be positive");

        threads = new Thread[k];
        for (int i = 0; i != k; ++i) {
            threads[i] = new Thread(new ThreadWorker());
            threads[i].setDaemon(true);
            threads[i].start();
        }
    }

    private void closeGeneric(boolean verify) {
        synchronized (this) {
            if (terminated) {
                if (verify)
                    throw new IllegalStateException("Pool is terminated allready");
                else
                    return;
            }
            
            terminated = true;
        }
        
        for (int i = 0; i != threads.length; ++i)
            try {
                threads[i].interrupt();
                threads[i].join();
            } catch (InterruptedException ex) {throw new RuntimeException(ex);}

        
    }
    
    /**
     * Terminated pool
     * @throws IllegalStateException if pool is allready terminated
     */
    public void shutdown() {
        closeGeneric(true);
    }

    /**
     * Almost same as shutdown, but allows to use with try-with-resources.
     * Unlike shutdown doesn't throws if pool was terminated.
     */
    @Override
    public void close() {
        closeGeneric(false);
    }

    
    /**
     * Adds new computation to the pool
     * @param <R> computation type
     * @param supplier function to compute
     * @return future corresponding to the given function
     * @throws IllegalStateException if pool is allready terminated
     */
    public <R> LightFuture<R> add(Supplier<R> supplier) {
        LightFuture<R> res = new LightFuture<R>(this, supplier);

        synchronized (this) {
            if (terminated)
                throw new IllegalStateException("Pool is terminated");
            queue.add(res);
            this.notifyAll();
        }

        return res;
    }

    /**
     * Helping function, returns next job to handle or null if pool is terminating.
     * @return new future to compute or null.
     */
    protected LightFuture<?> getNextFuture() {
        synchronized (this) {
            while (queue.isEmpty())
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    return null;
                }

            return queue.poll();
        }
    }

    /**
     * Entry point for workers' main.
     */
    private class ThreadWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                LightFuture<?> future = getNextFuture();

                if (future == null) {
                    return; // pool has terminated.
                }

                future.compute();
            }
        }
    }
}
