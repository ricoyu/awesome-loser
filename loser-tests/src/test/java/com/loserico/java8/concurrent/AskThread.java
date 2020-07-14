package com.loserico.java8.concurrent;

import java.util.concurrent.CompletableFuture;

public class AskThread implements Runnable {
	private CompletableFuture<Integer> future = null;

	public AskThread(CompletableFuture<Integer> future) {
		this.future = future;
	}

	@Override
	public void run() {
		int myRe = 0;
		try {
			myRe = future.get() * future.get();
		} catch (Exception e) {
		}
		System.out.println(myRe);
	}

	public static void main(String[] args) throws InterruptedException {
		final CompletableFuture<Integer> future = new CompletableFuture<Integer>();
		new Thread(new AskThread(future)).start();
		// 模拟长时间的计算过程
		Thread.sleep(1000);
		// 告知完成结果
		future.complete(60);
	}
}