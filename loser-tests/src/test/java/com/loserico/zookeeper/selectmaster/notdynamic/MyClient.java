package com.loserico.zookeeper.selectmaster.notdynamic;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

/**
 * 静态Master选举
 * 两个客户端同时创建某个节点, 创建成功的就是Master
 * <p>
 * Copyright: Copyright (c) 2019-04-04 15:54
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
	private final static String MASTER_NODE = "/sishuok/myservergroup1/currentMaster";

	private ZooKeeper startZK(String hostPort, int sessionTime) {
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			zk = new ZooKeeper(hostPort, sessionTime, (event) -> {
				if (event.getState() == KeeperState.SyncConnected) {
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
			e.printStackTrace();
		}
	}

	private void showMaster() {
		byte[] rets = null;
		try {
			if (zk.exists(MASTER_NODE, event -> showMaster()) != null) {
//				rets = zk.getData(MASTER_NODE, true, new Stat());
				rets = zk.getData(MASTER_NODE, false, new Stat());
			} else {
				System.out.println("now no master");
				return;
			}
		} catch (Exception e) {
			return;
		}

		String master = new String(rets);
		System.out.println("now master==" + master);
	}

	public static void main(String[] args) throws InterruptedException {
		MyClient myClient = new MyClient();
		myClient.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 1000);

		// 功能处理
		myClient.showMaster();

		Thread.currentThread().join();

//		hello.stopZK();

	}

}
