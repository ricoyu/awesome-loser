package com.loserico.concurrent.blockingQueue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.*;

/**
 * A Queue that additionally supports operations that wait for the queue to become
 * non-empty when retrieving an element, and wait for space to become available in the
 * queue when storing an element.
 * 
 * BlockingQueue methods come in four forms, with different ways of handling
 * operations that cannot be satisfied immediately, but may be satisfied at some point
 * in the future: one throws an exception, the second returns a special value (either
 * null or false, depending on the operation), the third blocks the current thread
 * indefinitely until the operation can succeed, and the fourth blocks for only a
 * given maximum time limit before giving up. These methods are summarized in the
 * following table:
 * 
 * Summary of BlockingQueue methods
 * --------------------------------------------------------------------------------
 * 			Throws exception	Special value	Blocks			Times out
 * =======	================	=============	==============	===================
 * Insert	add(e)				offer(e)		put(e)			offer(e, time, unit)
 * Remove	remove()			poll()			take()			poll(time, unit)
 * Examine	element()			peek()			not applicable	not applicable
 * 
 * A BlockingQueue does not accept null elements. 不允许插入null值
 * Implementations throw NullPointerException on attempts to add, put or offer a null. 
 * A null is used as a sentinel value to indicate failure of poll operations.
 * 
 * BlockingQueue implementations are thread-safe. BlockingQueue 是线程安全的，但是addAll等批量操作方法不是线程安全的
 * All queuing methods achieve their effects atomically using internal locks or other forms of concurrency control. 
 * However, the bulk Collection operations addAll, containsAll, retainAll
 * and removeAll are not necessarily performed atomically unless specified
 * otherwise in an implementation. So it is possible, for example, for addAll(c)
 * to fail (throwing an exception) after adding only some of the elements in c.
 * <p>
 * Copyright: Copyright (c) 2018-06-17 16:51
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public class BlockingQueueTest2 {
	
	private static final AtomicInteger SEQ = new AtomicInteger(1);

	public static void main(String[] args) {
		BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
		
		new Thread("producer") {
			public void run() {
				try {
					SECONDS.sleep(6);
					log.info("[{}] Putting message: Hello World!", SEQ.getAndIncrement());
					queue.put("Hello World!");
					log.info("[{}] Message sent!", SEQ.getAndIncrement());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}.start();
		try {
			log.info("[{}] Waiting for a message...", SEQ.getAndIncrement());
			String message = queue.take();
			log.info("[{}] Received message: " + message, SEQ.getAndIncrement());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
