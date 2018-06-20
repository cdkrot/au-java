package me.cdkrot;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestCountDownLatch {
    public TestCountDownLatch() {}

    @Test
    public void onlyDecreaseTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i != 5; ++i)
            latch.countDown();
    }

    @Test
    public void onlyIncreaseTest() {
        CountDownLatch latch = new CountDownLatch();

        for (int i = 0; i != 5; ++i)
            latch.countUp();
    }

    @Test
    public void decreaseAndIncreaseTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch();
        for (int i = 0; i != 5; ++i)
            latch.countUp();

        for (int i = 0; i != 5; ++i)
            latch.countDown();

        latch.await();
    }

    @Test
    public void simpleMultithreadedIncreaseDecrease() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch();
        Thread thr = new Thread(() -> {
            for (int i = 0; i != 1000; ++i)
                try {
                    latch.countDown();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
        });

        thr.start();

        for (int i = 0; i != 1000; ++i)
            latch.countUp();

        thr.join();

        latch.await();
    }

    @Test
    public void asyncAwaitTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);

        Thread thr = new Thread(() -> {
            for (int i = 0; i != 1000; ++i)
                try {
                    latch.countDown();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
        });

        thr.start();

        latch.await();

        thr.join();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorCantBeNegativeTest() throws IllegalArgumentException {
        new CountDownLatch(-10);
    }

    @Test
    public void belowzeroTest() throws InterruptedException {
        final boolean[] happened = {false};
        CountDownLatch latch = new CountDownLatch(0);

        Thread thr = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {}

            happened[0] = true;
            latch.countUp();
        });

        thr.start();
        latch.countDown();
        thr.join();

        // Checking, that count down didn't happened until other thread increased.
        assertTrue(happened[0]);
    }
}
