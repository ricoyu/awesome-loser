package com.loserico.zookeeper;

import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkWatcher {
	
	private static final Logger log = LoggerFactory.getLogger(ZkWatcher.class);
	
	private String oldValue = "aaa";

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		ZooKeeper zookeeper = new ZooKeeper("192.168.102.106:2181", 5000,
				(event) -> {
					log.info("Receive watcher event: " + event);
					if (event.getState() == KeeperState.SyncConnected) {
						countDownLatch.countDown();
					}
				});
		countDownLatch.await();
		
		ZkWatcher zkWatcher = new ZkWatcher();
		zkWatcher.getNode(zookeeper, "/rico");
		Thread.currentThread().join();
	}
	
	private String getNode(final ZooKeeper zooKeeper, final String nodePath) throws KeeperException, InterruptedException {
		byte[] data = zooKeeper.getData(nodePath, event -> {
			System.out.println(event.getType());
			try {
				checkChange(zooKeeper, nodePath);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}, new Stat());
		this.oldValue = new String(data, UTF_8);
		System.out.println("Old value:" + this.oldValue);
		return this.oldValue;
	}
	
	private boolean checkChange(ZooKeeper zooKeeper, String nodePath) throws KeeperException, InterruptedException {
		byte[] data = zooKeeper.getData(nodePath, false, new Stat());
		String result = new String(data, UTF_8);
		if (this.oldValue.equals(result)) {
			System.out.println("No change");
			return false;
		} else {
			System.out.println("changed, old value:" + this.oldValue + ", new value:" + result);
			return true;
		}
	}
}
