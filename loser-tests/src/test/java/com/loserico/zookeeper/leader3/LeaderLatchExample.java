package com.loserico.zookeeper.leader3;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Rico Yu
 * @since 2016-12-25 20:51
 * @version 1.0
 *
 */
public class LeaderLatchExample {

	private static final Logger logger = LoggerFactory.getLogger(LeaderLatchExample.class);
	
	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("192.168.1.3:2181,192.168.1.4:2181,192.168.1.6:2181")
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();

		LeaderLatch latch = new LeaderLatch(client, "/task/leader");
		//让listener在单独的线程池中运行  
		Executor executor = Executors.newCachedThreadPool();
		//每个listener都用来执行角色变换的事件处理.  
		LeaderLatchListener latchListener = new LeaderLatchListener() {
			@Override
			public void isLeader() {
				System.out.println("I am leader...");
			}

			@Override
			public void notLeader() {
				System.out.println("I am not leader...");
			}
		};
		latch.addListener(latchListener, executor);
		latch.start();
		latch.await();//等待leader角色.  
		//在await退出之后,你需要通过其他手段继续关注leader状态变更.  
		System.out.println(latch.hasLeadership());
		Thread.sleep(5000);
		latch.close();
		Thread.sleep(2000);
		client.close();
	}
}
