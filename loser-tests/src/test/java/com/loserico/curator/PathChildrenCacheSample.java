package com.loserico.curator;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.curator.framework.CuratorFrameworkFactory.builder;

/**
 * <p>
 * Copyright: (C), 2020-10-10 14:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PathChildrenCacheSample {
	
	@SneakyThrows
	public static void main(String[] args) {
		String path = "/zk-book2";
		
		CuratorFramework client = builder()
				.connectString("localhost:2181")
				.sessionTimeoutMs(30000)
				.namespace("namespace")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		PathChildrenCache cache = new PathChildrenCache(client, path, true);
		cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
		
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
					
					case CHILD_ADDED:
						System.out.println("=====> CHILD_ADDED : " + event.getData().getPath() + "  数据:" + new String(event.getData().getData(), UTF_8));
						break;
					case CHILD_REMOVED:
						System.out.println("=====> CHILD_REMOVED : " + event.getData().getPath() + "  数据:" + new String(event.getData().getData(), UTF_8));
						break;
					case CHILD_UPDATED:
						System.out.println("=====> CHILD_UPDATED : " + event.getData().getPath() + "  数据:" + new String(event.getData().getData(), UTF_8));
						break;
					default:
						break;
				}
			}
		});
		
		client.create().withMode(CreateMode.PERSISTENT).forPath(path, "init".getBytes(UTF_8));
		Thread.sleep(1000);
		client.create().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
		Thread.sleep(1000);
		client.setData().forPath(path + "/c1", "I love you".getBytes(UTF_8));
		Thread.sleep(1000);
		client.delete().forPath(path + "/c1");
		Thread.sleep(1000);
		client.delete().forPath(path);
		Thread.sleep(10000);
		cache.close();
		client.close();
	}
}
