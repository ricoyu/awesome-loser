package com.loserico.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 迁移zookeeper节点及节点数据
 * 
 * @author wgzh159@163.com
 */
public class MoveZkNode {

	public static void main(String[] args) throws Exception {
		//旧zk配置
		ZooKeeper sourceZK = new ZooKeeper("118.178.252.68:2181", 60000, null);
		//新zk配置
		ZooKeeper destZK = new ZooKeeper("192.168.102.103:2181", 60000, null);
		//迁移的节点
		String node = "/finance-centre";
		List<String> children = sourceZK.getChildren(node, false);
		move(sourceZK, destZK, children, node);
		sourceZK.close();
		destZK.close();
	}

	private static void move(ZooKeeper sourceZK, ZooKeeper destZK, List<String> children, String parent)
			throws KeeperException, InterruptedException {
		if (children == null || children.isEmpty()) {
			return;
		} else {
			for (String child : children) {
				String c = parent + "/" + child;
				System.out.println(c);
				byte[] data = sourceZK.getData(c, false, null);
				if (destZK.exists(c, false) == null) {
					destZK.create(c, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				} else {
					destZK.setData(c, data, -1, null, null);
				}
				move(sourceZK, destZK, sourceZK.getChildren(c, false), c);
			}
		}
	}
}