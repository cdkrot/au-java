package me.cdkrot;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implements CountDown latch, also known as semaphore
 *
 */
public class CountDownLatch {
    private int curValue;
    private Lock lock;
    private Condition zeroCondition;
    private Condition nonzeroCondition;

    /**
     * Creates new CountDown latch
     * @param val initial value of the counter
     */
    public CountDownLatch(int val) {
        if (val < 0)
            throw new IllegalArgumentException("Should be positive");

        curValue = val;
        lock = new ReentrantLock();

        zeroCondition = lock.newCondition();
        nonzeroCondition = lock.newCondition();
    }

    /**
     * Creates new CountDown latch with initial value of zero
     */
    public CountDownLatch() {
        this(0);
    }

    /**
     * Waits until the counter is zero
     *
     * If thread was interrupted than await operation is not completed.
     *
     * @throws InterruptedException if the thread was interrupted
     */
    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (curValue != 0)
                zeroCondition.await();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Decrease counter by one
     * In case of counter being zero waits until it is at least one.
     *
     * If thread was interrupted no operation was performed.
     *
     * @throws InterruptedException if thread was interrupted
     */
    public void countDown() throws InterruptedException {
        lock.lock();
        try {
            while (curValue == 0)
                nonzeroCondition.await();

            curValue -= 1;
            if (curValue == 0)
                zeroCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Increases counter by one
     */
    public void countUp() {
        lock.lock();
        curValue += 1;

        if (curValue == 1)
            nonzeroCondition.signalAll();
        lock.unlock();
    }
}
