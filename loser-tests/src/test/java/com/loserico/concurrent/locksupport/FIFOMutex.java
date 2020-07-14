package com.loserico.concurrent.locksupport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 一个先进先出的互斥量, 即按顺序获取锁
 * <p>
 * Copyright: (C), 2020/3/31 11:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FIFOMutex {
	
	private final AtomicBoolean locked = new AtomicBoolean(false);
	private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();
	
	public void lock() {
		boolean wasInterrupted = false;
		Thread current = Thread.currentThread();
		waiters.add(current);
		
		// Block while not first in queue or cannot acquire lock
		while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
			LockSupport.park(this);
			
			/*
			 * ignore interrupts while waiting
			 * 如果当前线程被中断了, 设置wasInterrupted属性为true
			 */
			if (Thread.interrupted()) {
				wasInterrupted = true;
			}
		}
		
		/**
		 * 当前线程是头结点且成功设置了locked状态位
		 * 把当前线程移除出waiters队列, 因为已经获取了锁, 所以不需要在waiters队列中等待
		 */
		waiters.remove();
		// reassert interrupt status on exit
		if (wasInterrupted) {
			/*
			 * interrupt()只是将current线程的中断状态设为true, 不会真正停止线程
			 */
			current.interrupt();
		}
	}
	
	/**
	 * 解锁
	 * 将locked状态位设为false并唤醒队头的线程
	 */
	public void unlock() {
		locked.set(false);
		LockSupport.unpark(waiters.peek());
	}
}
