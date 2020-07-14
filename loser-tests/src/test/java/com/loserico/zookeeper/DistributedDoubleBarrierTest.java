package com.loserico.zookeeper;

import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributedDoubleBarrierTest {

	public static void main(String[] args) {
		String barrierPath = "/curator_double_barrier_path";
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		String connectString = "192.168.1.3:2181";
//		String connectString = "192.168.1.3:2181,192.168.1.4:2181,192.168.1.6:2181";
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connectString)
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrierPath, 5);
		for (int i = 0; i < 6; i++) {
			executorService.execute(() -> {
				try {
					MILLISECONDS.sleep(Math.round(Math.random() * 3000));
					System.out.println(Thread.currentThread().getName() + " 号进入barrier");
					barrier.enter(4, SECONDS);
//					barrier.enter();
					System.out.println("启动...");
					MILLISECONDS.sleep(Math.round(Math.random() * 3000));
					barrier.leave();
					System.out.println("退出...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
}
