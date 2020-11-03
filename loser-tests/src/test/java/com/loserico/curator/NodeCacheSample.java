package com.loserico.curator;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.curator.framework.CuratorFrameworkFactory.builder;

/**
 * <p>
 * Copyright: (C), 2020-10-10 13:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NodeCacheSample {
	
	@SneakyThrows
	public static void main(String[] args) {
		
		String path = "/zk-book/nodecache";
		
		CuratorFramework client = builder()
				.connectString("localhost:2181")
				.sessionTimeoutMs(30000)
				.namespace("nodecache")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		client.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.forPath(path, "init".getBytes(UTF_8));
		
		final NodeCache nodeCache = new NodeCache(client, path, false);
		nodeCache.start();
		
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				ChildData currentData = nodeCache.getCurrentData();
				String data = currentData == null ? "" : new String(currentData.getData(), UTF_8);
				System.out.println("=====> Node data update, new Data: " + data);
			}
		});
		
		client.setData().forPath(path, "I love you".getBytes(UTF_8));
		TimeUnit.SECONDS.sleep(12);
		client.delete().deletingChildrenIfNeeded().forPath(path);
		TimeUnit.SECONDS.sleep(1);
		nodeCache.close();
		client.close();
	}
}
