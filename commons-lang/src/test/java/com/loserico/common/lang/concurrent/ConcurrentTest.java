package com.loserico.common.lang.concurrent;

/**
 * <p>
 * Copyright: (C), 2021-05-18 11:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ConcurrentTest {
	
	//public static void main(String[] args) {
	//	Concurrent.execute(() -> {
	//		System.out.println("第一个任务开始执行...");
	//		try {
	//			TimeUnit.SECONDS.sleep(3);
	//		} catch (InterruptedException e) {
	//			e.printStackTrace();
	//		}
	//		System.out.println("第一个任务执行完成...");
	//	});
	//	Concurrent.execute(() -> {
	//		System.out.println("第二个任务开始执行...");
	//		if (true) {
	//			throw new RuntimeException("第二个任务执行失败");
	//		}
	//		System.out.println("第二个任务执行完成...");
	//	});
	//	Concurrent.await();
	//	System.out.println("所有任务执行完成");
	//}
	
	public static void main(String[] args) {
		FutureResult<String> futureResult = Concurrent.submit(() -> {
			return "hello";
		});

		FutureResult<String> futureResult1 = Concurrent.submit(() -> {
			if (true) {
				throw new RuntimeException("第二个任务执行失败");
			}
			return "world";
		});

		Concurrent.await();
		System.out.println("执行完毕: " + futureResult1.get());
	}
}
