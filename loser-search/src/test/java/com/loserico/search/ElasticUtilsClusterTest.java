package com.loserico.search;

import org.junit.Test;

public class ElasticUtilsClusterTest {

	@Test
	public void testClusterHealth() {
		String health = ElasticUtils.Cluster.health();
		System.out.println(health);
		String health1 = ElasticUtils.Cluster.health();
		System.out.println(health1);
	}
}
