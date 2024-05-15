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
		boolean deleted = ElasticUtils.Admin.deleteIndex("mytest");
		boolean created = ElasticUtils.Admin.createIndex("mytest")
				.settings()
				.numberOfShards(4)
				.numberOfReplicas(1)
				.indexRoutingAllocation("node_type", "hot") //只在hot节点上分配
				.thenCreate();
		
		String health = ElasticUtils.Cluster.health();
		System.out.println(health);

		deleted = ElasticUtils.Admin.deleteIndex("mytest");
		created = ElasticUtils.Admin.createIndex("mytest")
				.settings()
				.numberOfShards(3)
				.numberOfReplicas(1)
				//.indexRoutingAllocation("node_type", "hot") //只在hot节点上分配
				.thenCreate();

		boolean updated = Settings.update("mytest")
				.numberOfReplicas(0)
				.thenUpdate();
		
		health = Cluster.health();
		assertThat(health).isEqualTo("GREEN");
	}
}
