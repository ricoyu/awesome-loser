package com.loserico.concurrent.completablefuture;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CompletableFutureExample {

    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                SECONDS.sleep(1); // Simulate a long running task
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello, CompletableFuture!";
        });

        // 当异步任务完成时，执行下一个操作
        future.thenAccept(System.out::println);

        try {
            String result = future.get();
            System.out.println("get result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
