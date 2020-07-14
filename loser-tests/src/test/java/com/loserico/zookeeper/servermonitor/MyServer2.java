package com.loserico.zookeeper.servermonitor;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServer2 {
	private ZooKeeper zk = null;
	private final static String PARENT_NODE = "/sishuok/myservergroup1";

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

	private void stopZK(ZooKeeper zk) {
		try {
			zk.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void createNode(String nodePath, String nodeData, CreateMode cm) {
		try {
			zk.create(nodePath, nodeData.getBytes(), Ids.OPEN_ACL_UNSAFE, cm);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MyServer2 myServer2 = new MyServer2();
		myServer2.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 3000);

		try {
			if (myServer2.zk.exists(PARENT_NODE, false) == null) {
				myServer2.zk.create(PARENT_NODE, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} 
		} catch (KeeperException e) {
			log.error("", e);
		}
		// 功能处理
		myServer2.createNode(PARENT_NODE + "/server2", "sishuokServer222", CreateMode.EPHEMERAL);

		Thread.currentThread().join();
//		hello.stopZK(zk);

	}
}
