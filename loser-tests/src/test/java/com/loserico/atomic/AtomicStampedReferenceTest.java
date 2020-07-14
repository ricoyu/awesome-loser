package com.loserico.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * <p>
 * Copyright: (C), 2019/11/22 9:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AtomicStampedReferenceTest {
	
	private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<>(1, 0);
	
	/**
	 * AtomicStampedReference解决ABA问题
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Thread main = new Thread(() -> {
			int stamp = atomicStampedRef.getStamp(); //获取当前标识
			System.out.println("操作线程" + Thread.currentThread() + "stamp=" + stamp + ",初始值 a = " + atomicStampedRef.getReference());
			try {
				TimeUnit.MILLISECONDS.sleep(1000); //等待一秒, 一遍干扰线程执行
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//此时expectedReference未发生改变，但是stamp已经被修改了,所以CAS失败
			boolean isCasSuccess = atomicStampedRef.compareAndSet(1, 2, stamp, stamp + 1);
			System.out.println("操作线程" + Thread.currentThread() + "stamp=" + stamp + ",CAS操作结果: " + isCasSuccess);
		}, "主操作线程");
		
		Thread other = new Thread(() -> {
			int stamp = atomicStampedRef.getStamp();
			atomicStampedRef.compareAndSet(1, 2, stamp, stamp + 1);
			System.out.println("操作线程" + Thread.currentThread() + "stamp=" + atomicStampedRef.getStamp() + ",【increment】 ,值 = " + atomicStampedRef.getReference());
			stamp = atomicStampedRef.getStamp();
			atomicStampedRef.compareAndSet(2, 1, stamp, stamp + 1);
			System.out.println("操作线程" + Thread.currentThread() + "stamp=" + atomicStampedRef.getStamp() + ",【decrement】 ,值 = " + atomicStampedRef.getReference());
		}, "干扰线程");
		
		main.start();
		other.start();
	}
}
