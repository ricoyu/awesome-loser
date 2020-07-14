package com.loserico.zookeeper.leader4;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * LeaderLatch使用流程
 * 
 * recipes包里面提供了Leader选举实现，Leader选举的实现在org.apache.curator.framework.recipes.leader包中，这个包提供了两组Leader选举：
 * 
 * 1.LeaderLatch, LeaderLatchListener
 * 2.LeaderSelector, LeaderSelectorListener, LeaderSelectorListenerAdapter
 * 
 * 这两组类都可以实现Leader选举
 * 
 * 第一组使用起来非常简单，使用思路大致如下：假设你有3个节点，姑且叫做node0，node1，node2。
 * 你需要为每一个node创建一个CuratorFramework，LeaderLatch，LeaderLatchListener，如下：
 * 
 * node0:
 * 1.CuratorFramework client=CuratorFrameworkFactory.newClient(.....);client.start();
 * 2.new LeaderLatch(client,path)->addListener(LeaderLatchListener )->start()
 * 
 * node1:
 * 1.CuratorFramework client=CuratorFrameworkFactory.newClient(.....);client.start();
 * 2.new LeaderLatch(client,path)->addListener(LeaderLatchListener )->start()
 * 
 * node2:
 * 1.CuratorFramework client=CuratorFrameworkFactory.newClient(.....);client.start();
 * 2.new LeaderLatch(client,path)->addListener(LeaderLatchListener )->start()
 * 
 * 你首先要创建CuratorFramework，然后并启动它，一个CuratorFramework就是一个ZooKeeper客户端。
 * 然后创建LeaderLatch，并指定刚才创建的CuratorFramework和一个leaderPath，leaderPath是一个ZooKeepe路径，node0，node1，node2中的leaderPath必须一致。
 * 创建好LeaderLatch之后，需要为他注册一个LeaderLatchListener，如果某个node成为leader，那么会调用这个node的LeaderLatchListener的isLeader()，
 * 因此你可以在这里写自己的业务逻辑。
 * 
 * 最后，调用LeaderLatch的start()，这个LeaderLatch将参加选举了。
 * 
 * @author Rico Yu
 * @since 2016-12-25 20:51
 * @version 1.0
 * @on
 *
 */
public class LeaderDemo {

	public static void main(String[] args) throws Exception {
		List<LeaderLatch> leaders = new ArrayList<LeaderLatch>();
		List<CuratorFramework> clients = new ArrayList<CuratorFramework>();

		try {
			for (int i = 0; i < 10; i++) {
				final int index = i;
				CuratorFramework client = CuratorFrameworkFactory.newClient(
						"192.168.1.103:2181",
						new ExponentialBackoffRetry(20000, 3));
				clients.add(client);

				LeaderLatch leader = new LeaderLatch(client, "/francis/leader");
				leader.addListener(new LeaderLatchListener() {

					@Override
					public void isLeader() {
						System.out.println("Node " + index + " I am Leader");
					}

					@Override
					public void notLeader() {
						System.out.println("Node " + index + " I am not Leader");
					}
				});

				leaders.add(leader);

				client.start();
				leader.start();
			}

			Thread.sleep(Integer.MAX_VALUE);
		} finally {
			for (CuratorFramework client : clients) {
				CloseableUtils.closeQuietly(client);
			}

			for (LeaderLatch leader : leaders) {
				CloseableUtils.closeQuietly(leader);
			}

		}

		Thread.sleep(Integer.MAX_VALUE);
		System.out.println("Exit!");
	}

}