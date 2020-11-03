package com.loserico.curator;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.curator.framework.CuratorFrameworkFactory.builder;

/**
 * <p>
 * Copyright: (C), 2020-10-10 14:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TreeCacheSample {
	
	@SneakyThrows
	public static void main(String[] args) {
		String path = "/zk-book3";
		
		CuratorFramework client = builder()
				.connectString("localhost:2181")
				.sessionTimeoutMs(30000)
				.namespace("book3")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		TreeCache cache = new TreeCache(client, path);
		cache.start();
		
		//添加错误监听器
		cache.getListenable().addListener(new TreeCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				switch (event.getType()) {
					case INITIALIZED:
						System.out.println("=====> INITIALIZED :  初始化");
						break;
					case NODE_ADDED:
						System.out.println("=====> NODE_ADDED : " + event.getData().getPath() + "  数据:" + new String(event.getData().getData(), UTF_8));
						break;
					case NODE_REMOVED:
						System.out.println("=====> NODE_REMOVED : " + event.getData().getPath() + "  数据:" + new String(event.getData().getData(), UTF_8));
						if ("/zk-book3".equals(event.getData().getPath())) {
							throw new RuntimeException("=====> 测试异常监听UnhandledErrorListener");
						}
						break;
					case NODE_UPDATED:
						System.out.println("=====> NODE_UPDATED : " + event.getData().getPath() + "  数据:" + new String(event.getData().getData()));
						break;
					default:
						System.out.println("=====> treeCache Type:" + event.getType());
						break;
				}
			}
		});
		
		client.create().withMode(CreateMode.PERSISTENT).forPath(path, "init".getBytes());
		Thread.sleep(3000);
		client.create().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
		Thread.sleep(3000);
		client.setData().forPath(path + "/c1", "I love you".getBytes());
		Thread.sleep(3000);
		client.delete().forPath(path + "/c1");
		Thread.sleep(3000);
		client.delete().forPath(path);
		Thread.sleep(10000);
		cache.close();
		client.close();
	}
}
