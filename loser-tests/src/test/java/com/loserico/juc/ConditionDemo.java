package com.loserico.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    private int sharedCounter = 0;
    private static final int THRESHOLD = 5;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public static void main(String[] args) {
        ConditionDemo demo = new ConditionDemo();
        Thread t1 = new Thread(demo::increment);
        Thread t2 = new Thread(demo::waitForThreshold);

        t2.start();
        t1.start();
    }

    public void increment() {
        lock.lock();
        try {
            while (sharedCounter < THRESHOLD) {
                sharedCounter++;
                System.out.println("Incremented: " + sharedCounter);
                // 睡眠一会儿，模拟一些工作
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            // 达到阈值，发送信号给在条件上等待的线程
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void waitForThreshold() {
        lock.lock();
        try {
            while (sharedCounter < THRESHOLD) {
                System.out.println("Waiting for sharedCounter to reach " + THRESHOLD);
                condition.await();
            }
            System.out.println("Threshold reached!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

}
