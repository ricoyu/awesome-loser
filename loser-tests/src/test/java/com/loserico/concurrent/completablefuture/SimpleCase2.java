package com.loserico.concurrent.completablefuture;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class SimpleCase2 {

    @Test
    public void test() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        	return "Hello";
        });

        try {
            assertEquals("Hello", future.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
