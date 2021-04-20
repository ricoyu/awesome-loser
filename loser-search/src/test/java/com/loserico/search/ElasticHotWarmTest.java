package com.loserico.search;

import com.loserico.search.ElasticUtils.Cluster;
import com.loserico.search.ElasticUtils.Settings;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-04-10 9:22
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticHotWarmTest {
	
	@Test
	public void testAllocationOnHotNode() {
		boolean deleted = ElasticUtils.deleteIndex("mytest");
		boolean created = ElasticUtils.createIndex("mytest")
				.settings()
				.numberOfShards(3)
				.numberOfReplicas(1)
				.indexRoutingAllocation("node_type", "hot")
				.thenCreate();
		
		String health = Cluster.health("mytest");
		assertThat(health).isEqualTo("YELLOW");
		
		boolean updated = Settings.update("mytest")
				.numberOfReplicas(0)
				.thenUpdate();
		
		health = Cluster.health("mytest");
		assertThat(health).isEqualTo("GREEN");
	}
}
