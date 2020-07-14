package com.loserico.zookeeper.selectmaster.dynamic;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

/**
 * 动态Master选举
 * <p>
 * Copyright: Copyright (c) 2019-04-04 15:49
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public class MyClient {
	private ZooKeeper zk = null;
	private final static String PARENT_NODE = "/sishuok/myservergroup1";

	private ZooKeeper startZK(String hostPort, int sessionTime) {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			zk = new ZooKeeper(hostPort, sessionTime, (event) -> {
				if (event.getState() == KeeperState.SyncConnected) {
					showMaster();
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
		}
	}

	private void showMaster() {
		try {
			List<String> list = zk.getChildren(PARENT_NODE, true);
			System.out.println("list===" + list);

			TreeSet<String> serverList = new TreeSet<String>();

			for (String node : list) {
				serverList.add(node);
			}

			String currentMaster = "";
			if (serverList.size() > 0) {
				currentMaster = serverList.first();
			}

			if (currentMaster.trim().length() > 0) {
				byte[] rets = null;

				rets = zk.getData(PARENT_NODE + "/" + currentMaster, false, new Stat());
				String master = new String(rets);
				System.out.println("now master==" + master);
			} else {
				System.out.println("no master in the server group!");
			}
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MyClient myClient = new MyClient();
		myClient.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 20000);

		// 功能处理
		Thread.currentThread().join();

		// hello.stopZK();

	}

}
