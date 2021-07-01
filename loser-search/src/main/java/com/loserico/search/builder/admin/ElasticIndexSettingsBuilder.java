package com.loserico.search.builder.admin;

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
public final class ElasticIndexSettingsBuilder extends ElasticSettingsBuilder {
	
	private ElasticIndexBuilder elasticIndexBuilder;
	
	public ElasticIndexSettingsBuilder(ElasticIndexBuilder elasticIndexBuilder) {
		this.elasticIndexBuilder = elasticIndexBuilder;
	}
	
	/**
	 * 设置主分片数, 默认1
	 *
	 * @param numberOfShards
	 * @return SettingsBuilder
	 */
	public ElasticIndexSettingsBuilder numberOfShards(int numberOfShards) {
		super.numberOfShards(numberOfShards);
		return this;
	}
	
	/**
	 * 设置副本数, 默认0
	 *
	 * @param numberOfReplicas
	 * @return SettingsBuilder
	 */
	public ElasticIndexSettingsBuilder numberOfReplicas(int numberOfReplicas) {
		super.numberOfReplicas(numberOfReplicas);
		return this;
	}
	
	/**
	 * 设置这个索引默认的pipeline
	 *
	 * @param defaultPipeline
	 * @return SettingsBuilder
	 */
	public ElasticIndexSettingsBuilder defaultPipeline(String defaultPipeline) {
		super.defaultPipeline(defaultPipeline);
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
	public ElasticIndexSettingsBuilder indexRoutingAllocation(String key, String value) {
		super.indexRoutingAllocation(key, value);
		return this;
	}
	
	public ElasticIndexBuilder and() {
		elasticIndexBuilder.settings(this);
		return elasticIndexBuilder;
	}
	
	public boolean thenCreate() {
		elasticIndexBuilder.settings(this);
		return elasticIndexBuilder.create();
	}
}
