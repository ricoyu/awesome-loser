package com.loserico.search.builder;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.enums.Dynamic;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;

/**
 * <p>
 * Copyright: (C), 2021-01-03 14:41
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ElasticIndexBuilder {
	
	private CreateIndexRequestBuilder createIndexRequestBuilder;
	
	private AbstractMappingBuilder mappingBuilder;
	
	private ElasticSettingsBuilder settings;
	
	public ElasticIndexBuilder(CreateIndexRequestBuilder createIndexRequestBuilder) {
		this.createIndexRequestBuilder = createIndexRequestBuilder;
	}
	
	/**
	 * 通过MappingBuilder设置Index的Mapping, 默认dynamic为true
	 *
	 * @return IndexBuilder
	 */
	public ElasticIndexMappingBuilder mapping() {
		ElasticIndexMappingBuilder builder = new ElasticIndexMappingBuilder(this, Dynamic.TRUE);
		this.mappingBuilder = builder;
		return builder;
	}
	
	/**
	 * 通过MappingBuilder设置Index的Mapping
	 *
	 * @param dynamic
	 * @return IndexBuilder
	 */
	public ElasticIndexMappingBuilder mapping(Dynamic dynamic) {
		ElasticIndexMappingBuilder builder = new ElasticIndexMappingBuilder(this, dynamic);
		this.mappingBuilder = builder;
		return builder;
	}
	
	/**
	 * 通过MappingBuilder设置Index的Mapping
	 *
	 * @param mappingBuilder
	 * @return IndexBuilder
	 */
	public ElasticIndexBuilder mapping(AbstractMappingBuilder mappingBuilder) {
		this.mappingBuilder = mappingBuilder;
		return this;
	}
	
	/**
	 * 设置Index的Settings
	 *
	 * @param settings
	 * @return IndexBuilder
	 */
	public ElasticIndexBuilder settings(ElasticSettingsBuilder settings) {
		this.settings = settings;
		return this;
	}
	
	/**
	 * 设置Index的主分片数
	 *
	 * @return IndexBuilder
	 */
	public ElasticIndexSettingsBuilder settings() {
		ElasticIndexSettingsBuilder elasticSettingsBuilder = new ElasticIndexSettingsBuilder(this);
		elasticSettingsBuilder.numberOfShards(1);
		return elasticSettingsBuilder;
	}
	
	/**
	 * 创建index
	 *
	 * @return
	 */
	public boolean create() {
		if (mappingBuilder != null) {
			createIndexRequestBuilder.addMapping(ElasticUtils.ONLY_TYPE, mappingBuilder.build());
		}
		if (settings != null) {
			createIndexRequestBuilder.setSettings((org.elasticsearch.common.settings.Settings) ReflectionUtils.invokeMethod(settings, "build" ));
		}
		CreateIndexResponse response = createIndexRequestBuilder.get();
		
		return response.isAcknowledged();
	}
	
}
