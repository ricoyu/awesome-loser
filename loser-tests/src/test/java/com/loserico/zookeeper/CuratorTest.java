package com.loserico.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class CuratorTest {

	private static final Logger logger = LoggerFactory.getLogger(CuratorTest.class);

	private static final String CONN_STR = "localhost:2181";
//	private static final String CONN_STR = "192.168.1.3:2181,192.168.1.4:2181,192.168.1.6:2181";

	@Test
	public void testCreateSession() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				CONN_STR,
				5000,
				3000,
				retryPolicy);
		client.start();
		client.close();
		/*
		 * try { MILLISECONDS.sleep(Integer.MAX_VALUE); } catch (InterruptedException
		 * e) { e.printStackTrace(); }
		 */
	}

	@Test
	public void testFluentFassion() {
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(CONN_STR)
				.sessionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
		
		try {
			String result = client.create().forPath("/rico");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNamespace() {
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(CONN_STR)
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.namespace("namespace")
				.build();
		client.start();
		try {
			String result = client.create().forPath("/rico"); //真正路径是: namespace/rico
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ZKPaths提供了一些简单的API来构建ZNode路径、递归创建和删除节点等
	 */
	@Test
	public void testZKPaths() {
		String path = "/curator_zk_path";
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(CONN_STR)
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();

		try {
			ZooKeeper zookeeper = client.getZookeeperClient().getZooKeeper();
			// 注意这三个API只是进行字符串拼接、截取而已，不实际操作Zookeeper
			System.out.println(ZKPaths.fixForNamespace(path, "/sub"));
			System.out.println(ZKPaths.makePath(path, "/sub"));
			System.out.println(ZKPaths.getNodeFromPath("/curator_zk_path/sub1"));

			// 这也不实际操作Zookeeper哦
			PathAndNode pn = ZKPaths.getPathAndNode(path + "/sub1");
			System.out.println(pn.getPath());
			System.out.println(pn.getNode());

			String dir1 = path + "/child1";
			String dir3 = path + "/child3";
			String dir2 = path + "/child2";
			ZKPaths.mkdirs(zookeeper, dir1);
			ZKPaths.mkdirs(zookeeper, dir3);
			ZKPaths.mkdirs(zookeeper, dir2);
			System.out.println(ZKPaths.getSortedChildren(zookeeper, path));
			ZKPaths.deleteChildren(zookeeper, path, true);
		} catch (Exception e) {
			logger.error("msg", e);
		}

	}

	/**
	 * EnsurePath提供了一种能够确保数据节点存在的机制，多用于这样的业务场景中
	 * 
	 * 上层业务希望对数据节点进行一些操作，但是操作之前要确保该节点存在。基于Zookeeper提供的原始API接口，
	 * 为解决上述场景的问题，开发人员需要首先对该节点进行一个判断，如果节点不存在，那么就需要创建节点。
	 * 而于此同时，在分布式环境中，在A机器试图进行节点创建的过程中，由于并发操作的存在，另一台机器，如B机器，也在同时创建这个节点，
	 * 于是A机器创建的时候，可能会抛出诸如节点已经存在的异常。因此开发人员还必须对这些异常进行单独的处理，逻辑通常非常琐碎
	 * 
	 * EnsurePath正好可以用来解决这些烦人的问题，它采取静默的节点创建方式，其内部实现就是试图创建指定节点，如果节点已经存在，
	 * 那么就不进行任何操作，也不对外抛出异常，否则正常创建数据节点
	 * 
	 */
	@Test
	public void testEnsurePath() {
		String path = "/zk-book/c1";
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(CONN_STR)
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.namespace("namespace")
				.build();
		client.start();

		EnsurePath ensurePath = new EnsurePath(path);
	}

	@Test
	public void testBarrier() {
		String barrierPath = "/curator_barrier_path";
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(CONN_STR)
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.namespace("curator-test")
				.build();
		client.start();
		DistributedBarrier barrier = new DistributedBarrier(client, barrierPath);
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 5; i++) {
			executorService.execute(() -> {
				System.out.println(Thread.currentThread().getName() + " 号barrier设置");
				try {
					barrier.setBarrier();
					// 等待barrier释放
					barrier.waitOnBarrier();
					System.out.println("启动...");
				} catch (Exception e) {
					logger.error("msg", e);
				}
			});
		}

		try {
			SECONDS.sleep(3);
			// 释放barrier
			barrier.removeBarrier();
			SECONDS.sleep(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistributedDoubleBarrier() {
		String barrierPath = "/curator_double_barrier_path";
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 5; i++) {
			executorService.execute(() -> {
				try {
					CuratorFramework client = CuratorFrameworkFactory.builder()
							.connectString(CONN_STR)
							.sessionTimeoutMs(60000)
							.retryPolicy(new ExponentialBackoffRetry(1000, 3))
							.build();
					client.start();
					DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrierPath, 5);
					MILLISECONDS.sleep(Math.round(Math.random() * 3000));
					System.out.println(Thread.currentThread().getName() + " 号进入barrier");
					barrier.enter();
					System.out.println("启动...");
					MILLISECONDS.sleep(Math.round(Math.random() * 3000));
					barrier.leave();
					System.out.println("退出...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		try {
			SECONDS.sleep(9);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMasterSelector() throws InterruptedException {
		String masterSelectorPath = "/master_selector";
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
				.connectString(CONN_STR)
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();

		LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, masterSelectorPath,
				new LeaderSelectorListenerAdapter() {

					@Override
					public void takeLeadership(CuratorFramework client) throws Exception {
						System.out.println("成为Master!");
						SECONDS.sleep(3);
						System.out.println("完成Master操作，释放Master权利!");
					}
				});

//		leaderSelector.autoRequeue();
		leaderSelector.start();

		Thread.sleep(Integer.MAX_VALUE);
		
		leaderSelector.close();
		curatorFramework.close();
	}
}
