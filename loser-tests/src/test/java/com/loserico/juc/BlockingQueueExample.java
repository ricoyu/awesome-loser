package com.loserico.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueExample {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    System.out.println("生产者准备放入元素: " + i);
                    queue.put(i); // 阻塞方式放入元素
                    System.out.println("生产者放入元素: " + i);
                    TimeUnit.MILLISECONDS.sleep(400);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("消费者准备取出元素");
                    Integer item = queue.take(); // 阻塞方式取出元素
                    System.out.println("消费者取出元素: " + item);
                    TimeUnit.MILLISECONDS.sleep(600);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
