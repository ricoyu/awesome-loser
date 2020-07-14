package com.loserico.java8.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {
	//测试规模，调用一次getAndIncreaseX视作提供一次业务服务，记录提供TEST_SIZE次服务的耗时
	private static final int TEST_SIZE = 100000000;
	//客户线程数
	private static final int THREAD_COUNT = 10;
	//使用CountDownLatch让各线程同时开始
	private CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT + 1);

	private int n = 0;
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private long startTime;

	public void init() {
		startTime = System.nanoTime();
	}

	/**
	 * 使用AtomicInteger.getAndIncrement，测试结果为1.8比1.7有明显性能提升
	 * 
	 * @return
	 */
	private final int getAndIncreaseA() {
		int result = atomicInteger.getAndIncrement();
		if (result == TEST_SIZE) {
			System.out.println(System.nanoTime() - startTime);
			System.exit(0);
		}
		return result;
	}

	/**
	 * 使用synchronized来完成同步，测试结果为1.7和1.8几乎无性能差别
	 * 
	 * @return
	 */
	private final int getAndIncreaseB() {
		int result;
		synchronized (this) {
			result = n++;
		}
		if (result == TEST_SIZE) {
			System.out.println(System.nanoTime() - startTime);
			System.exit(0);
		}
		return result;
	}

	/**
	 * 使用AtomicInteger.compareAndSet在java代码层面做失败重试（与1.7的AtomicInteger.getAndIncrement的实现类似），
	 * 测试结果为1.7和1.8几乎无性能差别
	 * 
	 * @return
	 */
	private final int getAndIncreaseC() {
		int result;
		do {
			result = atomicInteger.get();
		} while (!atomicInteger.compareAndSet(result, result + 1));
		if (result == TEST_SIZE) {
			System.out.println(System.nanoTime() - startTime);
			System.exit(0);
		}
		return result;
	}

	public class MyTask implements Runnable {
		@Override
		public void run() {
			countDownLatch.countDown();
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (true)
				getAndIncreaseA();// getAndIncreaseB();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		AtomicTest at = new AtomicTest();
		for (int n = 0; n < THREAD_COUNT; n++)
			new Thread(at.new MyTask()).start();
		System.out.println("start");
		at.init();
		at.countDownLatch.countDown();
	}
}