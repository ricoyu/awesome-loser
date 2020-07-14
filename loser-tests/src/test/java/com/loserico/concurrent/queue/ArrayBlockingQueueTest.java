package com.loserico.concurrent.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <p>
 * Copyright: (C), 2019/11/22 16:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ArrayBlockingQueueTest {
	
	private BlockingQueue<Ball> blockingQueue = new ArrayBlockingQueue<>(1);
	
	/**
	 * 队列大小
	 *
	 * @return
	 */
	public int queueSize() {
		return blockingQueue.size();
	}
	
	/**
	 * 将球放入队列当中, 生产者
	 *
	 * @param ball
	 * @throws InterruptedException
	 */
	public void produce(Ball ball) throws InterruptedException {
		blockingQueue.put(ball);
	}
	
	/**
	 * 将球从队列当中拿出去，消费者
	 *
	 * @return
	 * @throws InterruptedException
	 */
	public Ball consume() throws InterruptedException {
		return blockingQueue.take();
	}
	
	public static void main(String[] args) {
		final ArrayBlockingQueueTest box = new ArrayBlockingQueueTest();
		
		/*
		 * 往箱子里面放入乒乓球
		 */
		Thread t1 = new Thread(() -> {
			int i = 0;
			for (; ; ) {
				Ball ball = new Ball();
				ball.setNumber("乒乓球编号:" + i);
				ball.setColor("yellow");
				try {
					//System.out.println(Thread.currentThread().getName() + ":准备往箱子里放入乒乓球:--->" + ball.getNumber());
					box.produce(ball);
					System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis() + ":往箱子里放入乒乓球:--->" + ball.getNumber());
					//System.out.println("put操作后，当前箱子中共有乒乓球:--->" + box.queueSize() + "个");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "生产者");
		
		Thread t2 = new Thread(() -> {
			for (; ; ) {
				try {
					//System.out.println(System.currentTimeMillis() + "准备到箱子中拿乒乓球:--->");
					Ball ball = box.consume();
					System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis() + "拿到箱子中的乒乓球:--->" + ball.getNumber());
					//System.out.println("take操作后，当前箱子中共有乒乓球:--->" + box.queueSize() + "个");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "消费者");
		
		t1.start();
		t2.start();
	}
}
