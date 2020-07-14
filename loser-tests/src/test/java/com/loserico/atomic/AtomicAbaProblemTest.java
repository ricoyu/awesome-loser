package com.loserico.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Copyright: (C), 2019/11/22 9:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AtomicAbaProblemTest {
	
	private static AtomicInteger atomicInteger = new AtomicInteger(1);
	
	/**
	 * ABA问题举例
	 * 从马云账户偷偷转1亿到A06这个账户里面
	 * 然后用这一亿去炒股, 挣了5000万
	 * 然后再把一亿转回马云账户
	 * 自己就空手套白狼赚了5000万
	 * 
	 * ABA问题的本质是过程无法去跟踪
	 * @param args
	 */
	public static void main(String[] args) {
		Thread main = new Thread(() -> {
			int a = atomicInteger.get();
			System.out.println("操作线程" + Thread.currentThread().getName() + "--修改前操作数值:" + a);
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			boolean isCasSuccess = atomicInteger.compareAndSet(a, 2);
			if (isCasSuccess) {
				System.out.println("操作线程" + Thread.currentThread().getName() + "--Cas修改后操作数值:" + atomicInteger.get());
			} else {
				System.out.println("CAS修改失败");
			}
		}, "主线程");
		
		Thread other = new Thread(() -> {
			
			atomicInteger.incrementAndGet(); // 1+1 = 2;
			System.out.println("操作线程" + Thread.currentThread().getName() + "--increase后值:" + atomicInteger.get());
			atomicInteger.decrementAndGet(); // atomic-1 = 2-1;
			System.out.println("操作线程" + Thread.currentThread().getName() + "--decrease后值:" + atomicInteger.get());
		}, "干扰线程");
		
		main.start();
		other.start();
	}
}
