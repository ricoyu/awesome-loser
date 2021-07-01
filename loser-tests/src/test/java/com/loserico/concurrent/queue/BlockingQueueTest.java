package com.loserico.concurrent.queue;

import com.loserico.common.lang.utils.StringUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * BlockingQueue分有界和无界两种队列
 * 无界队列
 * 1: LinkedBlockingDeque
 * <p>
 * Copyright: (C), 2019/12/2 17:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class BlockingQueueTest {
	
	@Test
	public void testUnboundedBlockingQueue() {
		/*
		 * LinkedBlockingQueue默认设置capacity为Integer.MAX_VALUE
		 * 因为无界, 添加元素永远不会阻塞
		 * 无界队列的消费端需要尽快消费, 不然很容易OOM
		 */
		BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
	}
	
	@Test
	public void testBoundedQueue() throws InterruptedException {
		/*
		 * 有界队列就是在new的时候传一个capacity设置好容量
		 */
		BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(10);
		for (int i = 0; i < 10; i++) {
			String e = "e" + i;
			System.out.println(blockingQueue.offer(e));;
			//blockingQueue.add(e);
			System.out.println(e);
		}
		//blockingQueue.add("a");
		//System.out.println(blockingQueue.offer("a"));
		blockingQueue.put("a");
		System.out.println("done");
	}
	
	@Test
	public void testRemote() {
		User u1 = new User("rico");
		User u2 = new User("俞雪华");
		User u3 = new User("rico");
		
		BlockingQueue<User> queue = new LinkedBlockingQueue<>(12);
		queue.add(u1);
		queue.add(u2);
		queue.add(u3);
		
		boolean remoted = queue.remove(new User("rico"));
		boolean remoted2 = queue.remove(new User("rico"));
		assertThat(queue.size() == 1);
		System.out.println(queue.poll());
		boolean remoted3 = queue.remove(new User("rico"));
		assertThat(remoted3 == false);
	}
	
	@Test
	public void testDrainTo() throws InterruptedException {
		BlockingQueue<User> queue = new LinkedBlockingQueue<>();
		
		Runnable target;
		Thread t1 = new Thread(() -> {
			while (true) {
				log.info("从Queue中DrainTo");
				List<User> users = new ArrayList<>();
				queue.drainTo(users);
				log.info("Size:{}", users.size());
			}
		});
		Thread t2 = new Thread(() -> {
			log.info("丢2个到Queue中");
			User u1 = new User("rico");
			User u2 = new User("俞雪华");
			queue.offer(u1);
			queue.offer(u2);
			
			try {
				TimeUnit.SECONDS.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			log.info("丢1个到Queue中");
			User u3 = new User("rico");
			queue.offer(u3);
		});
		
		t1.start();
		TimeUnit.SECONDS.sleep(3);
		t2.start();
	}
	
	@SneakyThrows
	@Test
	public void testTake() {
		BlockingQueue<User> queue = new LinkedBlockingQueue<>();
		
		Runnable target;
		Thread t1 = new Thread(() -> {
			while (true) {
				log.info("从Queue中Take");
				try {
					User user = queue.take();
					log.info("Size:{}", user);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(() -> {
			log.info("丢2个到Queue中");
			User u1 = new User("rico");
			User u2 = new User("俞雪华");
			queue.offer(u1);
			queue.offer(u2);
			
			try {
				TimeUnit.SECONDS.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			log.info("丢1个到Queue中");
			User u3 = new User("rico");
			queue.offer(u3);
		});
		
		t1.start();
		TimeUnit.SECONDS.sleep(3);
		t2.start();
		
		Thread.currentThread().join();
	}
	
	@Test
	public void test() {
		System.out.println(new Date(1624005395304L));
	}
	
	@Data
	private class User{
		private String name;
		
		public User(String name) {
			this.name = name;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			
			if (!(obj instanceof User)) {
				return false;
			}
			
			return StringUtils.equalTo(this.name, ((User)obj).getName());
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
