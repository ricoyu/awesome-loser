package com.loserico.sentinel;

import com.loserico.common.lang.concurrent.LoserExecutors;
import com.loserico.common.lang.utils.DateUtils;
import com.loserico.networking.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2023-03-18 20:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SentinelTest {
	
	/**
	 * 测试流控模式: 关联
	 */
	@Test
	public void testRelateRule() {
		ThreadPoolExecutor executor = LoserExecutors.of("order-pool")
				.corePoolSize(8)
				.maxPoolSize(18)
				.build();
		
		CountDownLatch countDownLatch = new CountDownLatch(100);
		for (int i = 0; i < 100; i++) {
			executor.execute(() -> {
				try {
					Object response = HttpUtils.post("http://localhost:8081/order/createOrder")
							.body("{\"userId\": \"1001\",\"commodityCode\": \"2001\",\"count\": 2,\"money\": 10}")
							.request();
				} catch (Exception e) {
					log.error("", e);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		
		for (int i = 0; i < 10; i++) {
			String response = HttpUtils.get("http://localhost:8081/order/findOrderByUserId/1").request();
			log.info("请求[findOrderByUserId]: {}", response);
		}
		
	}
	
	@Test
	public void testWarmUp() {
		ThreadPoolExecutor executor = LoserExecutors.of("order-pool")
				.corePoolSize(10)
				.maxPoolSize(18)
				.prestartAllCoreThreads()
				.build();
		
		CountDownLatch countDownLatch = new CountDownLatch(10);
		
		for (int i = 0; i < 10; i++) {
			executor.execute(() -> {
				try {
					Object response = HttpUtils.get("http://localhost:8081/order/findOrderByUserId/1").request();
					System.out.println(response);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		
		try {
			countDownLatch.await();
			System.out.println("开始休眠10秒");
			SECONDS.sleep(10);
			System.out.println("休眠结束, 应该一次可以通过10个请求");
			System.out.println("=====================================================");
			SECONDS.sleep(1);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		CountDownLatch countDownLatch1 = new CountDownLatch(10);
		
		for (int i = 0; i < 10; i++) {
			executor.execute(() -> {
				try {
					Object response = HttpUtils.get("http://localhost:8081/order/findOrderByUserId/1").request();
					System.out.println(response);
				} finally {
					countDownLatch1.countDown();
				}
			});
		}
		
		try {
		countDownLatch1.await();
		}catch (Exception e) {
			
		}
	}
	
	@Test
	public void testWarmUp2() {
		CountDownLatch countDownLatch = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int j = 0; j < 100; j++) {
						String response = HttpUtils.get("http://localhost:8081/order/findOrderByUserId/1").request();
						log.info(response);
						try {
							SECONDS.sleep(1);
							countDownLatch.countDown();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	@Test
	public void testqueueWait() {
		for (int j = 0; j < 1000; j++) {
			String response = HttpUtils.get("http://localhost:8081/order/findOrderByUserId/1").request();
			log.info(DateUtils.format(new Date()));
			log.info("Success");
			
		}
	}
	
	@Test
	public void testDegrade() {
		ThreadPoolExecutor executor = LoserExecutors.of("DEGRADE")
				.corePoolSize(5)
				.prestartAllCoreThreads()
				.build();
		
		CountDownLatch countDownLatch = new CountDownLatch(100);
		
		for (int i = 0; i < 100; i++) {
			executor.execute(() -> {
				Object response = HttpUtils.get("http://localhost:8080/portal/test").request();
				System.out.println(response);
				countDownLatch.countDown();
			});
		}
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testExceptionCounter() {
		CountDownLatch countDownLatch = new CountDownLatch(50);
		ThreadPoolExecutor executor = LoserExecutors.of("rico")
				.corePoolSize(10)
				.prestartAllCoreThreads()
				.build();
		for (int i = 0; i < 50; i++) {
			executor.execute(() -> {
				Object response = HttpUtils.get("http://localhost:8080/portal/test2").request();
				System.out.println(response);
				countDownLatch.countDown();
			});
		}
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
