package com.loserico.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

public class DistributedAtomicIntegerTest {

	
	public static void main(String[] args) {
		String path = "/curator_disatomicinteger_path";
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("192.168.1.3:2181,192.168.1.4:2181,192.168.1.6:2181")
				.sessionTimeoutMs(60000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.namespace("curator")
				.build();
		client.start();
		DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(client, path, new RetryNTimes(3, 1000));
		AtomicValue<Integer> av;
		try {
			av = distributedAtomicInteger.add(6);
			System.out.println("Result: " + av.succeeded());
			System.out.println("Pre Value: " + av.preValue());
			System.out.println("Post Value: " + av.postValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
