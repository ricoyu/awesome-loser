package com.loserico.zookeeper;

import org.junit.Before;
import org.junit.Test;

public class ZookeeperClientTest {
	
	private ZookeeperClient zookeeperClient;
	
	@Before
	public void setup() {
		zookeeperClient = ZookeeperClient.initialize("localhost:2181", 4000);
	}
	
	@Test
	public void testCreateEphemeral() {
		String value = zookeeperClient.createEphemeral("/com/loserico");
		System.out.println(value);
	}
	
	@Test
	public void testCreateEphemeralWithValue() {
		String value = zookeeperClient.createEphemeral("/sishuok/mutexlock/currentLock", "server1");
		System.out.println(value);
	}
	
	@Test
	public void testCreatePersistent() {
		String value = zookeeperClient.createPersistent("/com/loserico");
		System.out.println(value);
	}
	
	@Test
	public void testCreatePersistentWithValue() {
		String value = zookeeperClient.createPersistent("/com/loserico", "handsome");
		System.out.println(value);
	}

	@Test
	public void testRmr() {
		zookeeperClient.rmr("/com");
	}
	
	@Test
	public void testDelete() {
		zookeeperClient.delete("/cc");
	}
}
