package com.loserico.concurrent.delayQueue;

import com.google.common.primitives.Ints;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * https://www.baeldung.com/java-delay-queue
 *
 * <p>
 * Copyright: (C), 2020-09-29 17:18
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DelayQueueTest {
	
	/**
	 * To test the behavior of the DelayQueue, we'll create one producer thread and one consumer thread.
	 * <p>
	 * The producer will put() two objects onto the queue with 500 milliseconds delay. The test asserts that the consumer consumed two messages:
	 */
	@SneakyThrows
	@Test
	public void testGivenDelayQueue_whenProduceElement_thenShouldConsumeAfterGivenDelay() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		DelayQueue<DelayObject> queue = new DelayQueue<>();
		int numberOfElementsToProduce = 2;
		int delayOfEachProducedMessageMilliseconds = 500;
		DelayQueueConsumer consumer = new DelayQueueConsumer(queue, numberOfElementsToProduce);
		DelayQueueProducer producer = new DelayQueueProducer(queue, numberOfElementsToProduce, delayOfEachProducedMessageMilliseconds);
		
		executor.submit(producer);
		executor.submit(consumer);
		
		executor.awaitTermination(5, TimeUnit.SECONDS);
		executor.shutdown();
		
		assertEquals(consumer.numberOfConsumedElements.get(), numberOfElementsToProduce);
	}
	
	@SneakyThrows
	@Test
	public void testGivenDelayQueue_whenProduceElement_thenNotAbleToConsumeAfterGivenDelay() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		DelayQueue<DelayObject> queue = new DelayQueue<>();
		
		int numberOfElementsToProduce = 1;
		//Let's say that we have a producer that is producing an element that will expire in 10 seconds:
		int delayOfEachProducedMessageMilliseconds = 10_000;
		
		DelayQueueConsumer consumer = new DelayQueueConsumer(queue, numberOfElementsToProduce);
		DelayQueueProducer producer = new DelayQueueProducer(queue, numberOfElementsToProduce, delayOfEachProducedMessageMilliseconds);
		
		executor.submit(producer);
		executor.submit(consumer);
		
		/*
		 * We'll start our test, but it will terminate after 5 seconds. Due to the characteristics of the DelayQueue, the consumer
		 * will not be able to consume the message from the queue because the element hasn't expired yet:
		 * element要10秒才过期, 但是我们的线程池5秒就关闭了, 所以应该一个元素都消费不到
		 */
		executor.awaitTermination(5, TimeUnit.SECONDS);
		executor.shutdown();
		
		assertEquals(0, consumer.numberOfConsumedElements.get());
	}
	
	@SneakyThrows
	@Test
	public void testGivenDelayQueue_whenProduceElementWithImmediateExpiration_thenConsumeImmediate() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		DelayQueue<DelayObject> queue = new DelayQueue<>();
		
		int numberOfElementsToProduce = 1;
		//Let's say that we have a producer that is producing an element that will expire in 10 seconds:
		int delayOfEachProducedMessageMilliseconds = -10_000;
		
		DelayQueueConsumer consumer = new DelayQueueConsumer(queue, numberOfElementsToProduce);
		DelayQueueProducer producer = new DelayQueueProducer(queue, numberOfElementsToProduce, delayOfEachProducedMessageMilliseconds);
		
		executor.submit(producer);
		executor.submit(consumer);
		
		/*
		 * We'll start our test, but it will terminate after 5 seconds. Due to the characteristics of the DelayQueue, the consumer
		 * will not be able to consume the message from the queue because the element hasn't expired yet:
		 * element要10秒才过期, 但是我们的线程池5秒就关闭了, 所以应该一个元素都消费不到
		 */
		executor.awaitTermination(5, TimeUnit.SECONDS);
		executor.shutdown();
		
		assertEquals(1, consumer.numberOfConsumedElements.get());
	}
	
	@NoArgsConstructor
	@AllArgsConstructor
	private class DelayQueueProducer implements Runnable {
		
		private DelayQueue<DelayObject> queue;
		private int numberOfElementsToProduce;
		private int delayOfEachProducedMessageMilliseconds;
		
		@SneakyThrows
		@Override
		public void run() {
			for (int i = 0; i < numberOfElementsToProduce; i++) {
				DelayObject object = new DelayObject(UUID.randomUUID().toString(), delayOfEachProducedMessageMilliseconds);
				System.out.println("Put object: " + object);
				try {
					queue.put(object);
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}
	
	@NoArgsConstructor
	private class DelayQueueConsumer implements Runnable {
		
		private DelayQueue<DelayObject> queue;
		private int numberOfElementsToTake;
		private AtomicInteger numberOfConsumedElements = new AtomicInteger(0);
		
		public DelayQueueConsumer(DelayQueue<DelayObject> queue, int numberOfElementsToTake) {
			this.queue = queue;
			this.numberOfElementsToTake = numberOfElementsToTake;
		}
		
		@SneakyThrows
		@Override
		public void run() {
			for (int i = 0; i < numberOfElementsToTake; i++) {
				DelayObject object = queue.take();
				numberOfConsumedElements.incrementAndGet();
				System.out.println("Consumer take: " + object);
			}
		}
	}
	
	private class DelayObject implements Delayed {
		
		private String data;
		
		/**
		 * this is a time when the element should be consumed from the queue
		 */
		private Long startTime;
		
		DelayObject(String data, long delayInMilliseconds) {
			this.data = data;
			this.startTime = System.currentTimeMillis() + delayInMilliseconds;
		}
		
		/**
		 * return the remaining delay associated with this object in the given time unit.
		 *
		 * @param unit
		 * @return
		 */
		@Override
		public long getDelay(TimeUnit unit) {
			long diff = startTime - System.currentTimeMillis();
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}
		
		/**
		 * We also need to implement the compareTo() method,
		 * because the elements in the DelayQueue will be sorted according to the expiration time.
		 * The item that will expire first is kept at the head of the queue and the element with the
		 * highest expiration time is kept at the tail of the queue:
		 *
		 * @param o
		 * @return
		 */
		@Override
		public int compareTo(Delayed o) {
			return Ints.saturatedCast(this.startTime = ((DelayObject) o).startTime);
		}
		
		@Override
		public String toString() {
			return "{" + "data='" + data + '\'' + ", startTime=" + startTime + '}';
		}
	}
}
