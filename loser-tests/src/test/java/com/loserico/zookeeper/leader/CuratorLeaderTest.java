package com.loserico.zookeeper.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;
import org.junit.Test;

/**
 * curator和zkclient都是zookeeper的开源客户端工具，比zookeeper提供的原生客户端更简洁。
 * leader选举是zookeeper的重要功能之一，用curator来实现zookeeper的leader选举会更简单。
 * 当集群里的某个服务down机时，我们要从slave结点里选出一个作为新的master，这时就需要一套能在分布式环境中自动协调的Leader选举方法。
 * Curator提供了LeaderSelector监听器实现Leader选举功能。同一时刻，只有一个Listener会进入takeLeadership()方法，说明它是当前的Leader。
 * 当Listener从takeLeadership()退出时就说明它放弃了“Leader身份”，这时Curator会利用Zookeeper再从剩余的Listener中选出一个新的Leader。
 * autoRequeue()方法使放弃Leadership的Listener有机会重新获得Leadership，如果不设置的话放弃了的Listener是不会再变成Leader的。
 * 
 * @author Rico Yu
 * @since 2016-12-25 20:45
 * @version 1.0
 *
 */
public class CuratorLeaderTest {

	/** Zookeeper info */
	private static final String ZK_ADDRESS = "192.168.1.3:2181,192.168.1.4:2181,192.168.1.6:2181";
	private static final String ZK_PATH = "/zktest";
	
	@Test
	public void testDelete() {
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
				.connectString(ZK_ADDRESS)
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		curatorFramework.start();
		try {
			curatorFramework.delete().forPath(ZK_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		curatorFramework.close();
	}

	public static void main(String[] args) throws InterruptedException {
		LeaderSelectorListener listener = new LeaderSelectorListener() {
			// 进入takeLeadership 方法说明当前节点获得leader身份，退出该方法就说明它放弃了leader身份
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.out.println(Thread.currentThread().getName() + " take leadership!");
				// takeLeadership() method should only return when leadership is being relinquished.
//				Thread.sleep(ThreadLocalRandom.current().nextLong(6000));
				System.out.println(Thread.currentThread().getName() + " relinquish leadership!");
			}

			public void stateChanged(CuratorFramework client, ConnectionState state) {
				System.out.println(state);
			}
		};
		/*new Thread(() -> {
			registerListener(listener);
		}).start();

		new Thread(() -> {
			registerListener(listener);
		}).start();*/

		new Thread(() -> {
			registerListener(listener);
		}).start();

		Thread.sleep(Integer.MAX_VALUE);
	}

	private static void registerListener(LeaderSelectorListener listener) {
		// 1.Connect to zk
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				ZK_ADDRESS,
				new RetryNTimes(10, 5000));
		client.start();

		// 2.Ensure path
		try {
			new EnsurePath(ZK_PATH).ensure(client.getZookeeperClient());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3.Register listener
		LeaderSelector selector = new LeaderSelector(client, ZK_PATH, listener);
		selector.autoRequeue();
		selector.start();
	}

}