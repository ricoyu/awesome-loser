package com.loserico.concurrent.condition;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Copyright: (C), 2020-12-10 17:44
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ReentrantLockWithCondition {
	
	Stack<Integer> stack = new Stack<>();
	int capacity = 5;
	
	ReentrantLock lock = new ReentrantLock();
	Condition stackEmptyCondition = lock.newCondition();
	Condition stackFullCondition = lock.newCondition();
	
	public static void main(String[] args) {
		ReentrantLockWithCondition reentrantLockWithCondition = new ReentrantLockWithCondition();
		Producer producer = new Producer(reentrantLockWithCondition);
		Consumer consumer = new Consumer(reentrantLockWithCondition);
		
		Thread t1 = new Thread(producer, "Producer Thread");
		Thread t2 = new Thread(consumer, "Consumer Thread");
		
		t1.start();
		t2.start();
	}
	
	public void pushToStack(Integer item) {
		try {
			lock.lock();
			while (stack.size() == capacity) {
				System.out.println(Thread.currentThread().getName() + " 栈已满, 等待...");
				stackFullCondition.await();
			}
			System.out.println(Thread.currentThread().getName() + " 栈未满, 添加: " + item);
			stack.push(item);
			System.out.println(Thread.currentThread().getName() + " 通知消费者...");
			stackEmptyCondition.signalAll();
		} catch (InterruptedException e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}
	}
	
	public Integer pullFromStack() {
		try {
			lock.lock();
			while (stack.size() == 0) {
				System.out.println(Thread.currentThread().getName() + " 栈已空, 等待...");
				stackEmptyCondition.await();
			}
			Integer item = stack.pop();
			System.out.println(Thread.currentThread().getName() + " 栈未空, 取出: " + item);
			return item;
		} catch (InterruptedException e) {
			log.error("", e);
			throw new RuntimeException();
		} finally {
			System.out.println(Thread.currentThread().getName() + " 通知生产者...");
			stackFullCondition.signalAll();
			lock.unlock();
		}
	}
	
	private static class Producer implements Runnable {
		
		private final ReentrantLockWithCondition reentrantLockWithCondition;
		
		private AtomicInteger atomicInteger = new AtomicInteger(1);
		
		public Producer(ReentrantLockWithCondition reentrantLockWithCondition) {
			this.reentrantLockWithCondition = reentrantLockWithCondition;
		}
		
		@Override
		public void run() {
			while (true) {
				int value = atomicInteger.getAndIncrement();
				System.out.println("生产: " + value);
				reentrantLockWithCondition.pushToStack(value);
			}
		}
	}
	
	
	private static class Consumer implements Runnable {
		
		private final ReentrantLockWithCondition reentrantLockWithCondition;
		
		public Consumer(ReentrantLockWithCondition reentrantLockWithCondition) {
			this.reentrantLockWithCondition = reentrantLockWithCondition;
		}
		
		@Override
		public void run() {
			while (true) {
				Integer value = reentrantLockWithCondition.pullFromStack();
				System.out.println("消费: " + value);
			}
		}
	}
}
