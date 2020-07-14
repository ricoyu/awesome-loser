package com.loserico.java8.concurrent;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CompletableFutureTest {

	@Test
	public void testThenApply() {
		String result = CompletableFuture.supplyAsync(() -> "hello").thenApply(s -> s + " world").join();
		System.out.println(result);
	}

	@Test
	public void testThenRun() throws InterruptedException {
		CompletableFuture.supplyAsync(() -> {
			try {
				SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello";
		}).thenRun(() -> System.out.println("hello jack"));

		Thread.currentThread().join();
	}

	/**
	 * 结合两个CompletionStage的结果，进行转化后返回
	 * 
	 * public <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
	 * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
	 * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn,Executor executor);
	 * 
	 * 它需要原来的处理返回值，并且other代表的CompletionStage也要返回值之后，利用这两个返回值，进行转换后返回指定类型的值.
	 */
	@Test
	public void thenCombine() {
		long begin = System.currentTimeMillis();
		String result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello";
		}).thenCombine(CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "world";
		}), (s1, s2) -> s1 + " " + s2).join();
		long end = System.currentTimeMillis();
		System.out.println("Time elapsed: " + (end - begin));
		System.out.println(result);
	}
}
