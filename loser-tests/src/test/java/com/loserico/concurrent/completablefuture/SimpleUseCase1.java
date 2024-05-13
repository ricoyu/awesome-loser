package com.loserico.concurrent.completablefuture;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleUseCase1 {

    public static Future<String> caculateAsync() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newSingleThreadExecutor().execute(() -> {
        	//模拟任务执行耗时
            try {
                SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("任务完成");
            completableFuture.complete("Hello");
        });

        return completableFuture;
    }

    @Test
    public void test() {
        Future<String> future = caculateAsync();
        long currentSeconds = System.currentTimeMillis()/1000;
        System.out.println("准备拿数据: " + currentSeconds);
        String result = null;
        try {
            result = future.get();
            assertEquals("Hello", result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        currentSeconds = System.currentTimeMillis()/1000;
        System.out.println("拿到结果: " + result + " "+currentSeconds);
    }
}
