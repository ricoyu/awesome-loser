package com.loserico.curator;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.curator.framework.CuratorFrameworkFactory.builder;

/**
 * <p>
 * Copyright: (C), 2020-10-10 11:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CuratorEventWatchTest {
	
	/**
	 * 执行此程序之后，首先会对节点/p1注册一个Watcher监听事件，同时返回当前节点的内容信息。
	 * 随后改变节点内容为“new content”，此时触发监听事件，并打印出监听事件信息。
	 * 但当第二次改变节点内容时，监听已经失效，无法再次获得节点变动事件。
	 */
	@SneakyThrows
	@Test
	public void testEventWatch() {
		CuratorFramework client = builder()
				.connectString("localhost:2181")
				.sessionTimeoutMs(3000)
				.namespace("namespace")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		client.create().orSetData().forPath("/p1");
		
		byte[] bytes = client.getData().usingWatcher(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("监听器watchedEvent：" + event);
				System.out.println(event.getType());
			}
		}).forPath("/p1");
		System.out.println("监听节点内容：" + new String(bytes, UTF_8));
		
		//第一次变更节点数据
		client.setData().forPath("/p1", "第一次变更数据".getBytes(UTF_8));
		//第二次变更节点数据
		client.setData().forPath("/p1", "第二次变更数据".getBytes(UTF_8));
		
		client.close();
	}
	
	@SneakyThrows
	@Test
	public void testCuratorListener() {
		CuratorFramework client = builder()
				.connectString("localhost:2181")
				.sessionTimeoutMs(3000)
				.namespace("namespace")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		client.create().orSetData().forPath("/p1");
		
		CuratorListener listener = new CuratorListener() {
			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("监听事件触发，event内容为：" + event);
				byte[] bytes = event.getData();
				System.out.println("数据为: " + new String(bytes, UTF_8));
			}
		};
		
		client.getCuratorListenable().addListener(listener);
		//异步获取节点数据
		client.getData().inBackground().forPath("/p1");
		//变更节点内容
		client.setData().forPath("/p1", "你好p1".getBytes(UTF_8));
		
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	/*
	 * Curator引入了Cache来实现对Zookeeper服务端事件监听，Cache事件监听可以理解为一个本地缓存视图与远程Zookeeper视图的对比过程。
	 * Cache提供了反复注册的功能。Cache分为两类注册类型：节点监听和子节点监听。
	 */
	@SneakyThrows
	@Test
	public void testNodeCacheWatch() {
		CuratorFramework client = builder()
				.connectString("localhost:2181")
				.sessionTimeoutMs(3000)
				.namespace("namespace")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		String path = "/p1";
		
		final NodeCache nodeCache = new NodeCache(client, path);
		nodeCache.start();
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("监听事件触发");
				System.out.println("重新获得节点内容为：" + new String(nodeCache.getCurrentData().getData()));
			}
		});
		
		client.setData().forPath(path, "456".getBytes());
		client.setData().forPath(path, "789".getBytes());
		client.setData().forPath(path, "123".getBytes());
		client.setData().forPath(path, "222".getBytes());
		client.setData().forPath(path, "333".getBytes());
		client.setData().forPath(path, "444".getBytes());
		Thread.sleep(15000);
	}
	
	@SneakyThrows
	public static void main(String[] args) {
		CuratorFramework client = builder()
				.connectString("localhost:2181")
				.sessionTimeoutMs(3000)
				.namespace("namespace")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		String path = "/p1";
		
		final NodeCache nodeCache = new NodeCache(client, path);
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("监听事件触发");
				System.out.println("重新获得节点内容为：" + new String(nodeCache.getCurrentData().getData()));
			}
		});
		nodeCache.start();
		
		client.setData().forPath(path, "456".getBytes());
		client.setData().forPath(path, "789".getBytes());
		client.setData().forPath(path, "123".getBytes());
		client.setData().forPath(path, "222".getBytes());
		client.setData().forPath(path, "333".getBytes());
		client.setData().forPath(path, "444".getBytes());
		Thread.sleep(15000);
		
		nodeCache.close();
		client.close();
	}
}
