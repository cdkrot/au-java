package me.cdkrot.lazy;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;
import org.junit.Test; 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestLazyFactory {
    private static class TestSupplier<T> implements Supplier<T> {
        private int count = 0;
        private T result;
        
        private TestSupplier(T a) {
            result = a;
        }
            
        @Override
        public T get() {
            synchronized (this) {
                count += 1;
                if (count > 1)
                    throw new IllegalStateException("Called twice");
            }

            return result;
        }
    };
    
    @Test
    public void testSimpleLazy() {
        Lazy<String> lazy = LazyFactory.createLazy(new TestSupplier<String>("123"));

        for (int i = 0; i != 10; ++i)
            assertEquals(lazy.get(), "123");
    }

    @Test
    public void testSimpleNullLazy() {
        Lazy<String> lazy = LazyFactory.createLazy(new TestSupplier<String>(null));

        for (int i = 0; i != 10; ++i)
            assertEquals(lazy.get(), null);
    }

    @Test
    public void testMultithreaded() {
        for (int test = 0; test != 100; ++test) { // repeat 100 times for sure.
            ArrayList<Lazy<Integer>> lazies = new ArrayList<Lazy<Integer>>();

            for (int i = 0; i != 10; ++i)
                lazies.add(LazyFactory.createMultithreadedLazy(new TestSupplier<Integer>(i)));

            Thread threads[] = new Thread[10];
            for (int i = 0; i != 10; ++i) {
                Random rnd = new Random(i);
                
                threads[i] = new Thread() {
                        @Override
                        public void run() {
                            for (int cur = 0; cur != 100; ++cur) {
                                int p = rnd.nextInt(10);
                                assertEquals(lazies.get(p).get(), new Integer(p));
                            }
                        }
                    };

                threads[i].start();
            }

            for (int i = 0; i != 10; ++i)
                try {
                    threads[i].join();
                } catch (InterruptedException ex) {}
        }
    }

    
    @Test
    public void testMultithreadedNullLazy() {
        for (int test = 0; test != 100; ++test) { // repeat 100 times for sure.
            ArrayList<Lazy<String>> lazies = new ArrayList<Lazy<String>>();

            for (int i = 0; i != 10; ++i)
                lazies.add(LazyFactory.createMultithreadedLazy(new TestSupplier<String>(null)));

            Thread threads[] = new Thread[10];
            for (int i = 0; i != 10; ++i) {
                Random rnd = new Random(i);
                
                threads[i] = new Thread() {
                        @Override
                        public void run() {
                            for (int cur = 0; cur != 100; ++cur) {
                                int p = rnd.nextInt(10);
                                assertEquals(lazies.get(p).get(), null);
                            }
                        }
                    };

                threads[i].start();
            }

            for (int i = 0; i != 10; ++i)
                try {
                    threads[i].join();
                } catch (InterruptedException ex) {}
        }   
    }
}
