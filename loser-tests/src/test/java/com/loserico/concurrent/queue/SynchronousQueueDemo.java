package com.loserico.concurrent.queue;

import java.util.concurrent.SynchronousQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SynchronousQueueDemo {

    public static void main(String[] args) {

        SynchronousQueue<String> queue = new SynchronousQueue<>();
        new Thread() {
            public void run() {
                try {
                    System.out.println("Waiting for a message...");
                    SECONDS.sleep(2);
                    System.out.println("Take a message...");
                    String message = queue.take();
                    System.out.println("Received message: " + message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }.start();

        try {

            System.out.println("Putting message: Hello World!");
            queue.put("Hello World!");
            System.out.println("Hello World! Message sent!");
            queue.put("Hello Blocking queue!");
            System.out.println("Message sent!");
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
