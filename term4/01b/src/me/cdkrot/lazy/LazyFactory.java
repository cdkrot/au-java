package me.cdkrot.lazy;

import java.util.function.Supplier;

/**
 * Factory class for the Lazy interface.
 */
class LazyFactory {
    /**
     * Creates a new lazy
     * @param sup Function to generate the result with
     */
    public static <T> Lazy<T> createLazy(Supplier<T> sup) {
        return new LazyImpl<T>(sup);
    }

    
    /**
     * Creates a new lazy which works correctly in the multithreaded environment.
     * @param sup Function to generate the result with
     */
    public static <T> Lazy<T> createMultithreadedLazy(Supplier<T> sup) {
        return new LazyMultithreadedImpl<T>(sup);
    }

    private static class LazyImpl<T> implements Lazy<T> {
        private Supplier<T> supplier;
        private T result;
        
        private LazyImpl(Supplier<T> sup) {
            supplier = sup;

            if (supplier == null)
                throw new IllegalArgumentException("Supplier shouldn't be null");
        }

        @Override
        public T get() {
            if (supplier != null) {
                result = supplier.get();
                supplier = null;
            }

            return result;
        }
    }

    private static class LazyMultithreadedImpl<T> implements Lazy<T> {
        private volatile Supplier<T> supplier;
        private T result;
        
        private LazyMultithreadedImpl(Supplier<T> sup) {
            supplier = sup;

            if (supplier == null)
                throw new IllegalArgumentException("Supplier shouldn't be null");
        }

        @Override
        public T get() {
            if (supplier != null)
                synchronized (this) {
                    if (supplier != null) {
                        result = supplier.get();
                        supplier = null;
                    }
                }

            return result;
        }
    }
};
