package com.loserico.zookeeper;

import static java.nio.charset.StandardCharsets.*;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigService {

	private ZooKeeper zk = null;
	private static final String DB_NODE = "/sishuok/dburl";
	private String dbUrl = null;
	
	public static void main(String[] args) throws InterruptedException {
		ConfigService configService = new ConfigService();
		configService.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181", 200000);
		String dbUrl = "jdbc:mysql://localhost:3306/sql_demo?useSSL=false&useLegacyDatetimeCode=false&useCompression=true&useUnicode=true&autoReconnect=true&autoReconnectForPools=true&failOverReadOnly=false";
		configService.initNode(DB_NODE, dbUrl, CreateMode.EPHEMERAL);
		configService.getNode(DB_NODE);
		Thread.currentThread().join();
//		configService.stopZK();
	}

	private void initNode(String dbNode, String s, CreateMode mode) {
		try {
			Stat stat = zk.exists(dbNode, false);
			if (stat != null) {
				log.info("已经初始化过, 不再重新初始化");
				return;
			}
			zk.create(dbNode, s.getBytes(UTF_8), Ids.OPEN_ACL_UNSAFE, mode);
		} catch (KeeperException | InterruptedException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

	private ZooKeeper startZK(String hostPort, int sessionTime) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			zk = new ZooKeeper(hostPort, sessionTime, (event) -> {
				if (event.getState() == KeeperState.SyncConnected) {
					log.info("连接上zookeeper");
					countDownLatch.countDown();
				}
			});
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
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

	private void createNode(String nodePath, String nodeData, CreateMode cm) {
		try {
			zk.create(nodePath, nodeData.getBytes(), Ids.OPEN_ACL_UNSAFE, cm);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

	private String getNode(final String nodePath) {
		byte[] rets = null;
		try {
			rets = zk.getData(nodePath, event -> getNode(nodePath), new Stat());
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}

		this.dbUrl = new String(rets);
		log.info("拿到DB Url:{}", dbUrl);
		return this.dbUrl;
	}

}
