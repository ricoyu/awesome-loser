package com.loserico.concurrent.interrupt;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020/3/31 11:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InterruptTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		/**
		 * 正常的任务代码被封装在while循环中，每次执行完一遍任务代码就检查一下中断状态；一旦发生中断，则跳过while循环，直接执行后面的中断处理代码。
		 */
		Thread t = new Thread(() -> {
			// 若未发生中断，就正常执行任务
			while (!Thread.currentThread().isInterrupted()) {
				System.out.println("执行正常业务代码...");
			}
			
			System.out.println("处理中断后的代码...");
		});
		t.start();
		
		TimeUnit.SECONDS.sleep(6);
		/**
		 * 下面代码执行后会将t1对象的中断状态设为true，此时t1线程的正常任务代码执行完成后，
		 * 进入下一次while循环前Thread.currentThread.isInterrupted()的结果为true，此时退出循环，执行循环后面的中断处理代码。
		 */
		t.interrupt();
	}
}
