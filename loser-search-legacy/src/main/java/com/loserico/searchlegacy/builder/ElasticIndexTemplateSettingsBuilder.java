package com.loserico.searchlegacy.builder;

/**
 * 为IndexTemplate设置Settings
 * <p>
 * Copyright: (C), 2021-03-16 9:44
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ElasticIndexTemplateSettingsBuilder extends SettingsBuilder {
	
	private ElasticIndexTemplateBuilder elasticIndexTemplateBuilder;
	
	public ElasticIndexTemplateSettingsBuilder(ElasticIndexTemplateBuilder elasticIndexTemplateBuilder) {
		this.elasticIndexTemplateBuilder = elasticIndexTemplateBuilder;
	}
	
	/**
	 * 设置主分片数, 默认1
	 *
	 * @param numberOfShards
	 * @return SettingsBuilder
	 */
	public ElasticIndexTemplateSettingsBuilder numberOfShards(int numberOfShards) {
		super.numberOfShards(numberOfShards);
		return this;
	}
	
	/**
	 * 设置副本数, 默认0
	 *
	 * @param numberOfReplicas
	 * @return SettingsBuilder
	 */
	public ElasticIndexTemplateSettingsBuilder numberOfReplicas(int numberOfReplicas) {
		super.numberOfReplicas(numberOfReplicas);
		return this;
	}
	
	/**
	 * 设置这个索引默认的pipeline
	 *
	 * @param defaultPipeline
	 * @return SettingsBuilder
	 */
	public ElasticIndexTemplateSettingsBuilder defaultPipeline(String defaultPipeline) {
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
	public ElasticIndexTemplateSettingsBuilder indexRoutingAllocation(String key, String value) {
		super.indexRoutingAllocation(key, value);
		return this;
	}
	
	public ElasticIndexTemplateBuilder and() {
		elasticIndexTemplateBuilder.settings(this);
		return elasticIndexTemplateBuilder;
	}
	
	public boolean thenCreate() {
		elasticIndexTemplateBuilder.settings(this);
		return elasticIndexTemplateBuilder.create();
	}
}
