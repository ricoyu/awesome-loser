package com.loserico.zookeeper.servermonitor;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServer1 {
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
			e.printStackTrace();
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
		MyServer1 myServer1 = new MyServer1();
		myServer1.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 3000);

		try {
			if (myServer1.zk.exists(PARENT_NODE, false) == null) {
				myServer1.zk.create(PARENT_NODE, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} 
			
		} catch (KeeperException e) {
			log.error("", e);
		}
		// 功能处理
		// 注意zookeeper创建某个节点时, 其父节点必须已经存在, 否则会报错
		myServer1.createNode(PARENT_NODE + "/server1", "sishuokTest1", CreateMode.EPHEMERAL);

		Thread.currentThread().join();
//		hello.stopZK(zk);

	}
}
