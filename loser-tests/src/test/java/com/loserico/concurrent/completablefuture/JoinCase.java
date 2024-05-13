package com.loserico.concurrent.completablefuture;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;

public class JoinCase {

    @Test
    public void test() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                SECONDS.sleep(1);
                System.out.println("future1执行结束");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                SECONDS.sleep(2);
                System.out.println("future2执行结束");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello2";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                SECONDS.sleep(3);
                System.out.println("future3执行结束");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello3";
        });

        long current = System.currentTimeMillis()/1000;
        System.out.println("等待全部执行完毕: "+ current);
            String comnined = Stream.of(future1, future2, future3)
                    .map(CompletableFuture::join)
                    .collect(Collectors.joining(" "));
        current = System.currentTimeMillis()/1000;
        System.out.println("全部任务执行完毕: " + current+", 执行结果: "+ comnined);
    }
}
