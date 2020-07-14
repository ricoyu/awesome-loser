package com.loserico.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyWatcher {

	private String oldValue = "";
	private ZooKeeper zk = null;

	public static void main(String[] args) {
		MyWatcher hello = new MyWatcher();
		ZooKeeper zk = null;
		zk = hello.startZK("192.168.102.106:2181", 200000);

		// 功能处理
//		hello.createNode(zk, "/sishuok", "sishuokTest1",CreateMode.PERSISTENT);

		String s = hello.getNode("/sishuok");
		System.out.println("s==" + s);

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			log.error("", e);
		}

		hello.stopZK();
	}

	private ZooKeeper startZK(String hostPort, int sessionTime) {
		try {
			zk = new ZooKeeper(hostPort, sessionTime, new Watcher() {

				public void process(WatchedEvent event) {
					System.out.println("the startZK  wather===" + event);
					if (event.getType() == EventType.NodeChildrenChanged && "/sishuok".equals(event.getPath())) {
						showChildren("/sishuok");
					} else {
						showChildren("/sishuok");
					}
				}

			});
		} catch (Exception e) {
			log.error("", e);
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

	private void showChildren(String nodePath) {
		try {
			List<String> list = zk.getChildren(nodePath, true);
			System.out.println("list===" + list);
		} catch (KeeperException e) {
			log.error("", e);
		} catch (InterruptedException e) {
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

	private String getNode(final String nodePath) {
		byte[] rets = null;
		try {
			rets = zk.getData(nodePath, (event) -> checkChange(nodePath), new Stat());
		} catch (Exception e) {
			log.error("", e);
		}

		return new String(rets);
	}

	private boolean checkChange(final String nodePath) {
		byte[] rets = null;
		try {
			rets = zk.getData(nodePath, (event) -> checkChange(nodePath), new Stat());
		} catch (Exception e) {
			log.error("", e);
		}
		String retStr = new String(rets);
		if (this.oldValue.equals(retStr)) {
			System.out.println("no change!");
			return false;
		} else {
			System.out.println("oldValue==" + oldValue + " , newValue==" + retStr);
			this.oldValue = retStr;
			return true;
		}
	}

}
