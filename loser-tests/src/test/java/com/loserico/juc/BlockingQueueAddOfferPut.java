package com.loserico.juc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BlockingQueueAddOfferPut {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(2);
        /*boolean result = blockingQueue.add("hello");
        System.out.println(result);
        result = blockingQueue.add(" world");
        System.out.println(result);
        result = blockingQueue.add("hi Sexy Uncle");
        System.out.println(result);*/

        /*boolean result = blockingQueue.offer("hello");
        System.out.println(result);
        result = blockingQueue.offer(" world");
        System.out.println(result);
        result = blockingQueue.offer("hi Sexy Uncle");
        System.out.println(result);*/
        new Thread(() -> {
            try {
                SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String result = null;
            try {
                result = blockingQueue.take();
                System.out.println("从queue中拉取元素: " + result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        try {
            blockingQueue.put("hello");
            blockingQueue.put(" world");
            blockingQueue.put("hello Sexy Uncle");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
