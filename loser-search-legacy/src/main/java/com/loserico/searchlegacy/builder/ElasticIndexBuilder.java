package com.loserico.searchlegacy.builder;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;

import static com.loserico.searchlegacy.constants.ElasticConstants.ONLY_TYPE;

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
	
	private ElasticMappingBuilder mappingBuilder;
	
	private Settings settings;
	
	public ElasticIndexBuilder(CreateIndexRequestBuilder createIndexRequestBuilder) {
		this.createIndexRequestBuilder = createIndexRequestBuilder;
	}
	
	/**
	 * 通过MappingBuilder设置Index的Mapping
	 *
	 * @param mappingBuilder
	 * @return IndexBuilder
	 */
	public ElasticIndexBuilder mapping(ElasticMappingBuilder mappingBuilder) {
		this.mappingBuilder = mappingBuilder;
		return this;
	}
	
	/**
	 * 设置Index的Settings
	 *
	 * @param settings
	 * @return IndexBuilder
	 */
	public ElasticIndexBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	/**
	 * 创建index
	 *
	 * @return
	 */
	public boolean create() {
		if (mappingBuilder != null) {
			createIndexRequestBuilder.addMapping(ONLY_TYPE, mappingBuilder.build());
		}
		if (settings != null) {
			createIndexRequestBuilder.setSettings((org.elasticsearch.common.settings.Settings) ReflectionUtils.invokeMethod(settings, "build" ));
		}
		CreateIndexResponse response = createIndexRequestBuilder.get();
		
		return response.isAcknowledged();
	}
	
}
