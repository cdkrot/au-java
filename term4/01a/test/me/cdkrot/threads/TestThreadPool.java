package me.cdkrot.threads;

import java.util.ArrayList;
import java.util.Random;
import org.junit.Test; 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestThreadPool {
    @Test
    public void testThreadPoolOneThread() {
        ThreadPool pool = new ThreadPool(1);
        
        LightFuture<String> a = pool.add(() -> "123");
        LightFuture<String> b = pool.add(() -> "");
        LightFuture<String> c = pool.add(() -> null);

        assertEquals(a.get(), "123");
        assertEquals(a.get(), "123");
        assertEquals(b.get(), "");
        assertEquals(c.get(), null);
        
        assertTrue(c.isReady());

        pool.shutdown();
    }

    @Test
    public void testThreadPoolTwoThread() {
        ThreadPool pool = new ThreadPool(2);

        LightFuture<?> futures[] = new LightFuture<?>[10];

        for (int i = 0; i != 10; ++i) {
            int j = i;
            futures[i] = pool.add(() -> new Integer(j));
        }

        for (int i = 0; i != 10; ++i)
            assertEquals(futures[i].get(), new Integer(i));
        pool.shutdown();
    }


    @Test
    public void testConsequentialness() {
        ThreadPool pool = new ThreadPool(2);

        LightFuture<Integer> cur = pool.add(() -> new Integer(0));
        for (int i = 0; i != 100; ++i)
            cur = cur.thenApply((val) -> val + 1);

        assertEquals(cur.get(), new Integer(100));
        pool.shutdown();
    }

    @Test(expected=LightException.class)
    public void testLightException() {
        try (ThreadPool pool = new ThreadPool(2)) {
            LightFuture<Object> cur = pool.add(() -> {throw new RuntimeException("kek");});
            cur.get();
        }
    }

    @Test
    public void testMultithreaded() {
        ThreadPool pool = new ThreadPool(3);

        Thread threads[] = new Thread[10];
        for (int i = 0; i != 10; ++i) {
            int j = i;
            
            threads[i] = new Thread() {
                    @Override
                    public void run() {
                        ArrayList<LightFuture<Integer>> futures = new ArrayList<LightFuture<Integer>>();
                        ArrayList<Integer> expected = new ArrayList<Integer>();


                        futures.add(pool.add(() -> new Integer(1)));
                        expected.add(1);
                        Random rnd = new Random(j);

                        while (futures.size() < 500) {
                            if (rnd.nextInt(10) != 3) {
                                int i = rnd.nextInt(futures.size() - 1);
                                int d = rnd.nextInt(10);
                                
                                futures.add(futures.get(i).thenApply((x) -> (x + d) % 1000));
                                expected.add((expected.get(i) + d) % 1000);
                            } else {
                                int z = rnd.nextInt(1000);
                                futures.add(pool.add(() -> new Integer(z)));
                                expected.add(z);
                            }
                        }

                        for (int i = 0; i != 500; ++i)
                            assertEquals(futures.get(i).get(), expected.get(i));
                    }
                };
        }
        
        for (int i = 0; i != 10; ++i)
            threads[i].start();

        for (int i = 0; i != 10; ++i)
            try {
                threads[i].join();
            } catch (InterruptedException ex) {}

        pool.shutdown();
    }
}
