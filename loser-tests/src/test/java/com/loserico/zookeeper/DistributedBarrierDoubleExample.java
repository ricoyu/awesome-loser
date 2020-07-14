package com.loserico.zookeeper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributedBarrierDoubleExample {
	private static final int QTY = 5;
	private static final String PATH = "/examples/barrier";

	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("192.168.1.3:2181,192.168.1.4:2181,192.168.1.6:2181")
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		ExecutorService service = Executors.newFixedThreadPool(QTY);
		for (int i = 0; i < (QTY + 2); ++i) {
			final DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, PATH, QTY);
			final int index = i;
			Callable<Void> task = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					Thread.sleep((long) (3 * Math.random()));
					System.out.println("Client #" + index + " 等待");
					if (false == barrier.enter(5, TimeUnit.SECONDS)) {
						System.out.println("Client #" + index + " 等待超时！");
						return null;
					}
					System.out.println("Client #" + index + " 进入");
					Thread.sleep((long) (3000 * Math.random()));
					barrier.leave();
					System.out.println("Client #" + index + " 结束");
					return null;
				}
			};
			service.submit(task);
		}
		service.shutdown();
		service.awaitTermination(10, TimeUnit.MINUTES);
		client.close();
		System.out.println("OK!");
	}
}