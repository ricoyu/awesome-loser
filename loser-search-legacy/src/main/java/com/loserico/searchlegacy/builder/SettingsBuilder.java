package com.loserico.searchlegacy.builder;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 为索引设置Settings
 * <p>
 * Copyright: (C), 2021-03-16 9:44
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SettingsBuilder {
	
	/**
	 * number_of_shards
	 */
	private Integer numberOfShards;
	
	/**
	 * number_of_replicas
	 */
	private Integer numberOfReplicas;
	
	/**
	 * index.default_pipeline
	 * <p>
	 * The default ingest node pipeline for this index.<p>
	 * Index requests will fail if the default pipeline is set and the pipeline does not exist.<p>
	 * The default may be overridden using the pipeline parameter.<p>
	 */
	private String defaultPipeline;
	
	/**
	 * 在Elasticsearch节点上可以配置node.attr.my_node_type: hot<p>
	 * 将节点配置成Hot或者Warm节点, 然后在设置索引的时候, 可以指定这个<p>
	 * 索引需要存放到Hot还是Warm节点上<p>
	 * <pre>
	 * PUT logs-2019-06-27
	 * {
	 *   "settings": {
	 *     "num_of_shards": 3,
	 *     "num_of_replicas": 1
	 *     "index.routing,allocation.require.my_node_type": "hot"
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * 这个indexRoutingAllocationKey对应my_node_type
	 */
	private String indexRoutingAllocationKey;
	
	/**
	 * 这个indexRoutingAllocationValue对应hot
	 */
	private String indexRoutingAllocationValue;
	
	public static SettingsBuilder builder() {
		return new SettingsBuilder();
	}
	
	/**
	 * 设置主分片数, 默认1
	 *
	 * @param numberOfShards
	 * @return SettingsBuilder
	 */
	public SettingsBuilder numberOfShards(int numberOfShards) {
		this.numberOfShards = numberOfShards;
		return this;
	}
	
	/**
	 * 设置副本数, 默认0
	 *
	 * @param numberOfReplicas
	 * @return SettingsBuilder
	 */
	public SettingsBuilder numberOfReplicas(int numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
		return this;
	}
	
	/**
	 * 设置这个索引默认的pipeline
	 *
	 * @param defaultPipeline
	 * @return SettingsBuilder
	 */
	public SettingsBuilder defaultPipeline(String defaultPipeline) {
		this.defaultPipeline = defaultPipeline;
		return this;
	}
	
	/**
	 * 在Elasticsearch节点上可以配置node.attr.my_node_type: hot<p>
	 * 将节点配置成Hot或者Warm节点, 然后在设置索引的时候, 可以指定这个<p>
	 * 索引需要存放到Hot还是Warm节点上<p>
	 * <pre>
	 * PUT logs-2019-06-27
	 * {
	 *   "settings": {
	 *     "num_of_shards": 3,
	 *     "num_of_replicas": 1
	 *     "index.routing,allocation.require.my_node_type": "hot"
	 *   }
	 * }
	 * </pre>
	 */
	public SettingsBuilder indexRoutingAllocation(String key, String value) {
		this.indexRoutingAllocationKey = key;
		this.indexRoutingAllocationValue = value;
		return this;
	}
	
	private Settings build() {
		Builder builder = Settings.builder();
		if (numberOfShards != null) {
			builder.put("number_of_shards", numberOfShards);
		}
		if (numberOfReplicas != null) {
			builder.put("number_of_replicas", numberOfReplicas);
		}
		if (isNotBlank(defaultPipeline)) {
			builder.put("index.default_pipeline", defaultPipeline);
		}
		if (isNotBlank(indexRoutingAllocationKey) && isNotBlank(indexRoutingAllocationValue)) {
			builder.put("index.routing.allocation.require." + indexRoutingAllocationKey, indexRoutingAllocationValue);
		}
		return builder.build();
	}
	
}
