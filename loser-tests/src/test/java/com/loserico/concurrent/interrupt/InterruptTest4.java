package com.loserico.concurrent.interrupt;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

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
public class InterruptTest4 {
	
	volatile static boolean flag = false;
	
	/**
	 * 一个正在运行的线程，interrupt是不会中断的
	 *
	 * @param args
	 */
	@SneakyThrows
	public static void main(String[] args) {
		
		Thread thread = new Thread(() -> {
			while (true) {
				log.info(Thread.currentThread() + LocalDateTime.now().toString());
				if (flag) {
					try {
						log.error("线程开始isleep");
						TimeUnit.SECONDS.sleep(6);
					} catch (InterruptedException e) {
						log.error("线程被中断, 收到了InterruptedException", e);
						return;
					}
				}
			}
			
		});
		
		thread.start();
		Thread.sleep(1000);
		log.info("调用线程的interrupt()方法设置线程的中断状态");
		thread.interrupt();
		System.out.println(Thread.currentThread() + "线程" + thread + "中断状态：" + thread.isInterrupted());
		Thread.sleep(1000);
		log.info("让线程进入sleep");
		flag = true;
	}
}
