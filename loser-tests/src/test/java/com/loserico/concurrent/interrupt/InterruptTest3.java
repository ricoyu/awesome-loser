package com.loserico.concurrent.interrupt;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2021-07-25 16:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class InterruptTest3 {
	
	/**
	 * 一个正在运行的线程，interrupt是不会中断的
	 *
	 * @param args
	 */
	@SneakyThrows
	public static void main(String[] args) {
		Thread thread = new Thread(() -> {
			while (true) {
				log.info("当前线程{}的中断状态{}", Thread.currentThread().getName(), Thread.currentThread().isInterrupted());
				
				//让该循环持续一段时间，使上面的话打印次数少点
				long i = 0;
				long time = System.currentTimeMillis();
				while ((System.currentTimeMillis() - time < 1000)) {
					i++;
				}
				System.out.println("i=" + i);
			}
			
		}, "中断测试");
		
		thread.start();
		Thread.sleep(3000);
		thread.interrupt();
		System.out.println(Thread.currentThread() + "线程" + thread + "是否中断：" + thread.isInterrupted());
	}
}
