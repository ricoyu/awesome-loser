package org.loser.cache;

import com.loserico.cache.JedisUtils;
import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2019/10/25 17:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JedisUtilsTests {
	
	@Test
	public void testWarmUp() {
		try {
			Class.forName("com.loserico.cache.JedisUtils");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLPushBRpop() {
		JedisUtils.LIST.lpush("list:aa", "a");
	}
	
	@Test
	public void testLPush() {
		JedisUtils.LIST.lpush("ids-alert", IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\ids-alert-http-post.json"));
	}
	
	@Test
	public void testLPushIdsMetadata() {
		for (int i = 0; i < 3; i++) {
			JedisUtils.LIST.lpush("ids-metadata", IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\ids-metadata-pop3.json"));
		}
	}
	
	@Test
	public void testSendDgaMetadata() {
		//JedisUtils.LIST.lpush("dga-metadata", IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\dga-metadata-request.json"));
		//JedisUtils.LIST.lpush("dga-metadata", IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\dga-metadata-response.json"));
		JedisUtils.LIST.lpush("dga-metadata", IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\dga-metadata-response - failed.json"));
	}
	
	@SneakyThrows
	@Test
	public void testIdsTraffic() {
		int i = 0;
		while (i < 10) {
			NetFlowBean netFlowBean = new NetFlowBean();
			netFlowBean.setBytesRecvd(ThreadLocalRandom.current().nextLong(1000));
			netFlowBean.setMeshPort("ens224");
			netFlowBean.setPktsDrooped(ThreadLocalRandom.current().nextLong(100));
			netFlowBean.setPktsRecvd(ThreadLocalRandom.current().nextLong(1000));
			netFlowBean.setTs(new Date().getTime());
			//netFlowBean.setTs(DateUtils.toEpochMilis(LocalDateTime.of(2021, 1, 1, ThreadLocalRandom.current().nextInt(24), ThreadLocalRandom.current().nextInt(60))));
			
			JedisUtils.LIST.lpush("ids-traffic", netFlowBean);
			//TimeUnit.SECONDS.sleep(1L);
			i++;
		}
	}
	
	@Test
	public void testRpopIdsTraffic() {
		String brpop = JedisUtils.LIST.rpop("ids-traffic");
		System.out.println(brpop);
	}
	
	@Test
	public void testSet() {
		JedisUtils.set("k1", "aaa");
		Assert.assertEquals("aaa", JedisUtils.get("k1"));
		System.out.println(JedisUtils.get("k1"));
	}
	
	@Test
	public void testSetWithExpire() {
		Boolean success = JedisUtils.set("k1", "v1", 1, TimeUnit.MINUTES);
		System.out.println(success);
	}
	
	@Test
	public void testSetNX() {
		Boolean success = JedisUtils.setnx("k2", "v2", 1, TimeUnit.MINUTES);
		System.out.println(success);
	}
	
	@Test
	public void testSubscribe() {
	}
	
	@SneakyThrows
	@Test
	public void testIncrWithExpire() {
		Long value = JedisUtils.incr("retryCount", 1, TimeUnit.MINUTES);
		System.out.println(value);
		TimeUnit.SECONDS.sleep(20);
		System.out.println(JedisUtils.incr("retryCount", 1, TimeUnit.MINUTES));
	}
	
	@Test
	public void testPipelined() {
		/*List<String> users = JedisUtils.pipeline((pipeline) -> {
			for (int i = 0; i < 100; i++) {
				pipeline.lpop("ids-traffic");
			}
		});
		users.forEach(System.out::println);*/
		while (true) {
			List<String> users = JedisUtils.pipeline((pipeline) -> {
				for (int i = 0; i < 100; i++) {
					pipeline.lpop("ids-traffic");
				}
			});
			users.forEach(System.out::println);
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	/*@SneakyThrows
	public static void main(String[] args) {
		JedisPubSub jedisPubSub = JedisUtils.subscribe("channel:test", (channel, message) -> {
			log.info(message);
		});
		TimeUnit.SECONDS.sleep(10);
		JedisUtils.unsubscribe(jedisPubSub, "channel:test");
		log.info("UnSubscribed");
	}*/
	
	public static void main(String[] args) {
		System.out.println(new Date(1615290356000L));
		/*Runnable task = () -> {
			Lock lock = JedisUtils.blockingLock("lock1");
			try {
				lock.lock();
				log.info(Thread.currentThread().getName() + " locked");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			} finally {
				if (lock.locked()) {
					lock.unlock();
				}
				System.out.println("任务完成");
				
			}
		};
		
		Thread t1 = new Thread(task, "t1");
		Thread t2 = new Thread(task, "t2");
		
		t1.start();
		t2.start();*/
		
	}
	
/*	public static void main(String[] args) {
		Lock lock = JedisUtils.blockingLock("lock1");
		try {
			lock.lock();
			log.info(Thread.currentThread().getName() + " locked");
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (true) {
				throw new RuntimeException();
			}
		} finally {
			if (lock.locked()) {
				lock.unlock();
			}
			System.out.println("任务完成");
			
		}
	}*/
	
	@Test
	public void testDelGet() {
		JedisUtils.set("k2", "三少爷");
		String value = JedisUtils.get("k2");
		System.out.println("value " + value);
		assertThat(value, value.equals("三少爷"));
		
		String value2 = JedisUtils.delGet("k2");
		System.out.println("value2 " + value2);
		assertThat(value2, value2.equals(value));
		
		String value3 = JedisUtils.get("k2");
		System.out.println("value3 " + value3);
		assertTrue(value3 == null);
	}
	
	@Test
	public void testHashLen() {
		JedisUtils.del("hash-len");
		JedisUtils.HASH.hset("hash-len", "f1", "v1");
		assertEquals(1, JedisUtils.HASH.hlen("hash-len"));
		JedisUtils.HASH.hset("hash-len", "f2", "v2");
		assertEquals(2, JedisUtils.HASH.hlen("hash-len"));
		JedisUtils.HASH.hdel("hash-len", "f1");
		assertEquals(1, JedisUtils.HASH.hlen("hash-len"));
	}
	
	@Test
	public void testHDelGet() {
		JedisUtils.del("hash-delget");
		JedisUtils.HASH.hset("hash-delget", "f1", "v1");
		assertEquals("v1", JedisUtils.HASH.hdelGet("hash-delget", "f1"));
	}
	
	@Test
	public void testLPushLimit() {
		/*JedisUtils.del("jobs");
		long size = LIST.lpushLimit("jobs", 6, "a", "b");
		assertEquals(2, size);
		
		size = JedisUtils.LIST.lpushLimit("jobs", 6, "c", "d", "e", "f", "g");
		assertEquals(6, size);
		
		JedisUtils.del("jobs");
		size = LIST.lpushLimit("jobs", 6, "a", "b", "v", "aasd", "g", "e", "t");
		assertEquals(6, size);*/
		
		JedisUtils.LIST.lpushLimit("gen_rule_file_tasks", 1, "");
	}
	
	@Test
	public void testQueueListener() {
		JedisUtils.LIST.brpop("jobs_list", (key, message) -> {
			System.out.println(message);
		});
	}
}
