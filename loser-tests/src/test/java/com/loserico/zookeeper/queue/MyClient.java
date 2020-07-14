package com.loserico.zookeeper.queue;

import java.util.List;
import java.util.TreeSet;

import org.apache.zookeeper.ZooKeeper;

import com.loserico.zookeeper.ZookeeperClient;

/**
 * 需要手工设置/queue-test/sishuok/myqueue1num 2
 * 先运行client, 然后再运行MyServer1, MyServer2
 * <p>
 * Copyright: Copyright (c) 2019-04-09 16:20
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class MyClient {
	private ZookeeperClient zookeeperClient;
	private ZooKeeper zk = null;
	private final static String PARENT_NODE = "/sishuok/myqueue1";
	private boolean isAll = false;

	private ZooKeeper startZK(String hostPort, int sessionTime) {
		zookeeperClient = ZookeeperClient.initialize(hostPort, sessionTime, (client, event) -> {
			showQueueMember();
		});
		zk = zookeeperClient.zk();
		return zk;
	}

	private void stopZK() {
		try {
			zk.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getQueueMember() {
		return zookeeperClient.getInteger("/sishuok/myqueue1num", false);
	}

	private void showQueueMember() {
		try {
			//watcher是true, 所以ZookeeperClient.initialize方法中设置的Watcher会再次被执行
			List<String> list = zookeeperClient.getChildren(PARENT_NODE, true);
			System.out.println("list===" + list);

			if (isAll || this.getQueueMember() == list.size()) {
				isAll = true;

				TreeSet<String> serverList = new TreeSet<String>();

				for (String node : list) {
					serverList.add(node);
				}

				String currentQueueMember = "";
				if (serverList.size() > 0) {
					currentQueueMember = serverList.first();
				}

				if (currentQueueMember.trim().length() > 0) {
					String qm = zookeeperClient.getStr(PARENT_NODE + "/" + currentQueueMember, false);
					System.out.println("now execute ==" + qm);

					//这里节点删除的时候也会触发ZookeeperClient.initialize方法中设置的Watcher
					this.deleteQueueMember(PARENT_NODE + "/" + currentQueueMember);
				} else {
					System.out.println("no master in the server group!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteQueueMember(String nodePath) {
		zookeeperClient.delete(nodePath);
	}

	public static void main(String[] args) {
		MyClient hello = new MyClient();
		hello.startZK("192.168.102.104:2181,192.168.102.106:2181,192.168.102.107:2181/queue-test", 20000);

		// 功能处理

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// hello.stopZK();

	}

}
