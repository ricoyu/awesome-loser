package com.loserico.zookeeper;

import com.loserico.json.jackson.JacksonUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

public class ZooKeeperTest {
	private static final Logger logger = LoggerFactory.getLogger(ZooKeeperTest.class);

	ZooKeeper zookeeper;

	@Before
	public void setup() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
//			String connectString = "118.178.252.68:2181";
			String connectString = "localhost:2181";
//			String connectString = "192.168.1.164:2181";
//			String connectString = "192.168.1.3:2181, 192.168.1.4:2181, 192.168.1.6:2181";
			zookeeper = new ZooKeeper(connectString, 5000,
					(event) -> {
						logger.info("Receive watcher event: " + event);
						if (event.getState() == KeeperState.SyncConnected) {
							countDownLatch.countDown();
						}
					});
			countDownLatch.await();
			System.out.println("========= Done! ==========");
		} catch (IOException | InterruptedException e) {
			logger.error("msg", e);
		}
	}
	
	@After
	public void tearDown() {
		if (zookeeper != null) {
			try {
				zookeeper.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testCreate() {
		try {
			String result = zookeeper.create("/rico", "rico".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info(result);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateSishuOK() {
		try {
			String result = zookeeper.create("/sishuok", "sishuoktest1".getBytes(UTF_8), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSishuOK() throws KeeperException, InterruptedException {
		Stat stat = new Stat();
		byte[] data = zookeeper.getData("/sishuok", false, stat);
		String s = new String(data, UTF_8);
		System.out.println(s);
		System.out.println(JacksonUtils.toJson(stat));
	}
	
	@Test
	public void testDelete() {
		try {
			zookeeper.delete("/finance-centre/ticketno/SUB", 18);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
