package com.loserico.zookeeper.lock.mutex;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.ZooKeeper;

import com.loserico.zookeeper.ZookeeperClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServer2 {
	private ZooKeeper zk;
	private ZookeeperClient zookeeperClient = null;
	private final static String PARENT_NODE = "/sishuok/mutexlock";

	private boolean alreadyExecute = false;

	private ZookeeperClient startZK(String hostPort, int sessionTime) {
		this.zookeeperClient = ZookeeperClient
				.initialize("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", sessionTime, (client, event) -> {
//					if (!alreadyExecute) {
						try {

							List<String> list = client.getChildren(PARENT_NODE, true);
							if (list.size() == 0) {
								createNode(PARENT_NODE + "/currentLock", "server2", CreateMode.EPHEMERAL);
							}
							getLock();
						} catch (Exception e) {
							if (e instanceof NoNodeException) {
								
							}
							log.error("", e);
						}
//					}
				});
		return zookeeperClient;
	}

	private void stopZK(ZooKeeper zk) {
		try {
			zk.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void createNode(String nodePath, String nodeData, CreateMode cm) {
		zookeeperClient.create(nodePath, nodeData, cm);
	}

	private void getLock() {
		try {
			String lock = zookeeperClient.getStr(PARENT_NODE + "/currentLock", false);

			System.out.println("now lock==" + lock);

			if ("server2".equals(lock)) {
				alreadyExecute = true;
				System.out.println("now server22222 execute service !!!" + System.currentTimeMillis());

				try {
					Thread.sleep(3000L);
				} catch (Exception err) {
					log.error("", err);
				}

				System.out.println("now server2 unlock");
				unLock();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void unLock() {
		try {
			zookeeperClient.delete(PARENT_NODE + "/currentLock");
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static void main(String[] args) {
		MyServer2 hello = new MyServer2();
		ZookeeperClient zookeeperClient = hello.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 200000);

		// 功能处理
		hello.createNode(PARENT_NODE + "/currentLock", "server2", CreateMode.EPHEMERAL);

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (Exception e) {
			log.error("", e);
		}
//		hello.stopZK(zk);

	}
}
