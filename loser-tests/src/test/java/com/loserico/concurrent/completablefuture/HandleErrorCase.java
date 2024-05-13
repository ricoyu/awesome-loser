package com.loserico.concurrent.completablefuture;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class HandleErrorCase {

    @Test
    public void test() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
        	if (true) {
                throw new RuntimeException("异步错误");
            }
            return "hello normal";
        }).handle((s, e) -> {
            if (s != null) {
                return s;
            } else {
                e.printStackTrace();
                return "hello abnormal";
            }
        });

        try {
            assertEquals("hello abnormal", completableFuture.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
