package com.loserico.zookeeper.servermonitor;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import lombok.extern.slf4j.Slf4j;

/**
 * 先启动MyMonitor
 * 然后启动MyServer1, 观察MyMonitor输出, 应该可以看到list===[server1]
 * 然后启动MyServer2, 观察MyMonitor输出, 应该可以看到list===[server2, server1]
 * 关掉MyServer1, 观察MyMonitor输出, 应该可以看到list===[server2]
 * <p>
 * Copyright: Copyright (c) 2019-04-04 15:36
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public class MyMonitor {
	private ZooKeeper zk = null;
	private final static String PARENT_NODE = "/sishuok/myservergroup1";

	private ZooKeeper startZK(String hostPort, int sessionTime) {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			zk = new ZooKeeper(hostPort, sessionTime, (event) -> {
				if (event.getState() == KeeperState.SyncConnected) {
					showChildren();
					countDownLatch.countDown();
				}
			});
			countDownLatch.await();
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}

		return zk;
	}

	private void stopZK() {
		try {
			zk.close();
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

	private void showChildren() {
		try {
			List<String> list = zk.getChildren(PARENT_NODE, true);
			System.out.println("list===" + list);
		} catch (KeeperException e) {
			log.error("", e);
		} catch (InterruptedException e) {
			log.error("", e);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MyMonitor myMonitor = new MyMonitor();
		myMonitor.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 3000);

		// 功能处理
		Thread.currentThread().join();

//		hello.stopZK();

	}

}
