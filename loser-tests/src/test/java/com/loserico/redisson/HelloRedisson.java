package com.loserico.redisson;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020/3/28 13:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HelloRedisson {
	
	private static RedissonClient redisson;
	
	@BeforeClass
	public static void SingleInit() {
		Config config = new Config();
		config.setLockWatchdogTimeout(1000);
		config.useSingleServer()
				.setAddress("redis://localhost:6379")
				.setPassword("deepdata$");
		redisson = Redisson.create(config);
	}
	
	public static void clusterInit() {
		Config config = new Config();
		//config.setTransportMode(TransportMode.EPOLL);
		config.setTransportMode(TransportMode.NIO);
		config.setCodec(new JsonJacksonCodec());
		//SSL方式
		//config.useClusterServers().addNodeAddress("rediss://192.168.100.101:6380");
		config.useClusterServers()
				.setPassword("deepdata$")
				.setScanInterval(2000)
				.addNodeAddress("redis://192.168.100.101:6380", "redis://192.168.100.102:6380") //一次指定多个node
				.addNodeAddress("redis://192.168.100.103:6380"); //或者add多次, 都可以添加集群节点
				
		
		redisson = Redisson.create(config);
	}
	
	@Test
	public void testCodec() {
	}
	
	/**
	 * 测试可以连接上Redis集群
	 */
	@Test
	public void testRedissonClusterConnect() {
		Assert.assertTrue(redisson != null);
	}
	
	/**
	 * lock()获取锁, 如果有第二个线程也lock同样的锁, 第一个就被覆盖掉了, 这种锁有什么用呢?
	 */
	@SneakyThrows
	@Test
	public void testTraditionalLock() {
		RLock lock = redisson.getLock("mylock");
		
		// traditional lock method
		log.info("传统方式获取锁");
		lock.lock();
		log.info("成功加锁");
		
		//TimeUnit.SECONDS.sleep(4);
		
		log.info("解锁...");
		lock.unlock();
	}
	
	
	@SneakyThrows
	@Test
	public void testTraditionalLockAssertBlocked() {
		RLock lock = redisson.getLock("mylock");
		
		// traditional lock method
		log.info("传统方式获取锁");
		lock.lock();
		log.info("成功加锁");
		
		//TimeUnit.SECONDS.sleep(4);
		
		log.info("解锁...");
		lock.unlock();
	}
	
	/**
	 * 加锁, 如果一时获取不到锁, 最多等待10秒
	 */
	@SneakyThrows
	@Test
	public void testTryLock() {
		RLock lock = redisson.getLock("tryLock");
		boolean locked = lock.tryLock(10, TimeUnit.SECONDS);
		log.info("加锁"+(locked ? "成功" : "失败"));
	}
}
