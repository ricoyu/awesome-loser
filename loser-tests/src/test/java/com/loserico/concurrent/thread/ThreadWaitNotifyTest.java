package com.loserico.concurrent.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.baeldung.com/java-wait-notify
 * <p>
 * Copyright: (C), 2021-07-30 17:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadWaitNotifyTest {
	
	@Slf4j
	static class Data {
		
		private String packet;
		
		// True if receiver should wait
		// False if sender should wait
		private boolean transfer = true;
		
		public synchronized void send(String packet) {
			while (!transfer) {
				try {
					wait();
				} catch (InterruptedException e) {
					/*
					 * 在catch了后, Thread.currentThread().isInterrupted()已经被重置为false
					 * 下面直接调用当前线程的interrupt(), 是为了把interrupted标志位设为true, 下次线程进入可中断状态(如wait())时会立刻被中断
					 */
					Thread.currentThread().interrupt();
					log.error("Thread interrupted", e);
				}
				transfer = false;
				this.packet = packet;
				notifyAll();
			}
		}
		
		public synchronized String receive() {
			while (transfer) {
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.error("Thread interrupted", e);
				}
			}
			transfer = true;
			
			notifyAll();
			return packet;
		}
	}
}
