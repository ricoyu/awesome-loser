package com.loserico.search.builder;

import com.loserico.search.ElasticUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;

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
public class IndexBuilder {
	
	private CreateIndexRequestBuilder createIndexRequestBuilder;
	
	private MappingBuilder mappingBuilder;
	
	private Settings settings;
	
	public IndexBuilder(CreateIndexRequestBuilder createIndexRequestBuilder) {
		this.createIndexRequestBuilder = createIndexRequestBuilder;
	}
	
	/**
	 * 通过MappingBuilder设置Index的Mapping
	 *
	 * @param mappingBuilder
	 * @return IndexBuilder
	 */
	public IndexBuilder mapping(MappingBuilder mappingBuilder) {
		this.mappingBuilder = mappingBuilder;
		return this;
	}
	
	/**
	 * 设置Index的Settings
	 *
	 * @param settings
	 * @return IndexBuilder
	 */
	public IndexBuilder settings(Settings settings) {
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
			createIndexRequestBuilder.addMapping(ElasticUtils.ONLY_TYPE, mappingBuilder.build());
		}
		if (settings != null) {
			createIndexRequestBuilder.setSettings(settings);
		}
		CreateIndexResponse response = createIndexRequestBuilder.get();
		
		return response.isAcknowledged();
	}
	
}
