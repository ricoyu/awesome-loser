package com.loserico.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

/**
 * 实现负载均衡获取服务地址
 * <p>
 * Copyright: Copyright (c) 2019-04-04 12:39
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public class BalancedConfigService {

	private ZooKeeper zk = null;
	private final static String PARENT_NODE = "/sishuok/storeService";
	private final static String CHILDREN_PREFIX = "sub";
	private int subCount = 5;

	private List<String> serverNodeList = new ArrayList<String>();
	private int currentServiceIndex = 0;

	public static void main(String[] args) throws InterruptedException {
		BalancedConfigService configService = new BalancedConfigService();
		configService.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 200000);
		for (int i = 0; i < 100; i++) {
			String service = configService.getService();
			log.info("当前的Service: {}", service);
		}
		Thread.currentThread().join();
	}

	public String getService() {
		currentServiceIndex = currentServiceIndex + 1;
		for (int i = currentServiceIndex; i <= subCount; i++) {
			if (serverNodeList.contains(CHILDREN_PREFIX + currentServiceIndex)) {
				return getNode(CHILDREN_PREFIX + currentServiceIndex);
			} else {
				currentServiceIndex = currentServiceIndex + 1;
			}
		}

		for (int i = 1; i <= subCount; i++) {
			if (serverNodeList.contains(CHILDREN_PREFIX + i)) {
				currentServiceIndex = i;
				return getNode(CHILDREN_PREFIX + i);
			}
		}
		return "NoSub";
	}

	private String getNode(String subNode) {
		byte[] bs = null;
		try {
			bs = zk.getData(PARENT_NODE + "/" + subNode, false, new Stat());
		} catch (KeeperException e) {
			log.error("", e);
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		return new String(bs);
	}

	private ZooKeeper startZK(String hostPort, int sessionTimeout) {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			this.zk = new ZooKeeper(hostPort, sessionTimeout, (event) -> {
				if (event.getState() == KeeperState.SyncConnected) {
					countDownLatch.countDown();
					initServerList();
				}
			});
			countDownLatch.await();
			return zk;
		} catch (IOException | InterruptedException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

	private void stopZK(ZooKeeper zk) {
		try {
			zk.close();
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

	private void initServerList() {
		try {
			this.serverNodeList = zk.getChildren(PARENT_NODE, true); // true表示watcher留着, 下次节点有变化继续触发
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
}
