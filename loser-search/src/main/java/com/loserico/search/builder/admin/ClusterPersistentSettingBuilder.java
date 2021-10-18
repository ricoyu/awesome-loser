package com.loserico.search.builder.admin;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.enums.cluster.AllocationEnable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;

/**
 * 集群相关Settings
 * <p>
 * Copyright: (C), 2021-04-18 8:19
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ClusterPersistentSettingBuilder {
	
	private ClusterSettingBuilder clusterSettingBuilder;
	
	/**
	 * cluster.routing.allocation.enable
	 */
	private AllocationEnable allocationEnable;
	
	private Integer searchMaxBuckets = 10000;
	
	public ClusterPersistentSettingBuilder(ClusterSettingBuilder clusterSettingBuilder) {
		this.clusterSettingBuilder = clusterSettingBuilder;
		ReflectionUtils.setField("persistentSettingBuilder", clusterSettingBuilder, this);
	}
	
	public ClusterPersistentSettingBuilder routingAllocationEnable(AllocationEnable allocationEnable) {
		this.allocationEnable = allocationEnable;
		return this;
	}
	
	public ClusterPersistentSettingBuilder searchMaxBuckets(Integer searchMaxBuckets) {
		this.searchMaxBuckets = searchMaxBuckets;
		return this;
	}
	
	private Settings build() {
		Builder builder = Settings.builder();
		if (allocationEnable != null) {
			builder.put("cluster.routing.allocation.enable", allocationEnable);
		}
		if (searchMaxBuckets != null) {
			builder.put("search.max_buckets", searchMaxBuckets);
		}
		return builder.build();
	}
	
	public ClusterSettingBuilder and() {
		return clusterSettingBuilder;
	}
	
	public boolean thenUpdate() {
		return clusterSettingBuilder.update();
	}
}
