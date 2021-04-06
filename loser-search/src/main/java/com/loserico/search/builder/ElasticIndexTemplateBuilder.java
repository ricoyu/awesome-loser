package com.loserico.search.builder;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.enums.Dynamic;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequestBuilder;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright: (C), 2021-01-06 9:07
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ElasticIndexTemplateBuilder {
	
	private TransportClient client;
	
	/**
	 * Index Template的名字
	 */
	private String name;
	
	private List<String> patterns = new ArrayList<>();
	
	/**
	 * order越低, 优先级越低, 即同一个设置会被优先级更高的Index Template覆盖
	 */
	private int order;
	
	private Integer version;
	
	/**
	 * 索引的settings, 常用的比如有
	 * <ol>
	 * <li/>number_of_shards
	 * <li/>number_of_replicas
	 * </ol>
	 */
	private Settings settings;
	
	/**
	 * 添加Mappings
	 */
	private ElasticIndexTemplateMappingBuilder mappingBuilder;
	
	private ElasticIndexTemplateBuilder() {
	}
	
	/**
	 * 创建一个IndexTemplateBuilder, 并指定Index Template的名字
	 *
	 * @param name
	 * @return
	 */
	public static ElasticIndexTemplateBuilder newInstance(TransportClient client, String name) {
		ElasticIndexTemplateBuilder builder = new ElasticIndexTemplateBuilder();
		builder.client = client;
		builder.name = name;
		return builder;
	}
	
	/**
	 * 这个Index Template匹配的Index的表达式
	 *
	 * @param patterns
	 * @return
	 */
	public ElasticIndexTemplateBuilder patterns(String... patterns) {
		Objects.requireNonNull(patterns, "patterns can not be null");
		this.patterns = asList(patterns);
		return this;
	}
	
	/**
	 * order越低, 优先级越低, 即同一个设置会被优先级更高的Index Template覆盖
	 *
	 * @param order
	 * @return
	 */
	public ElasticIndexTemplateBuilder order(int order) {
		this.order = order;
		return this;
	}
	
	public ElasticIndexTemplateBuilder version(Integer version) {
		this.version = version;
		return this;
	}
	
	
	/**
	 * 索引的settings, 常用的比如有
	 * <ol>
	 * <li/>number_of_shards
	 * <li/>number_of_replicas
	 * </ol>
	 * <p>
	 * 可以用Settings.builder()逐项设置
	 *
	 * @param settings
	 * @return
	 */
	public ElasticIndexTemplateBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	/**
	 * 为索引模板设置Settings
	 * @param numOfShards
	 * @return ElasticIndexTemplateSettingsBuilder
	 */
	public ElasticIndexTemplateSettingsBuilder settings(int numOfShards) {
		ElasticIndexTemplateSettingsBuilder elasticIndexTemplateSettingsBuilder = new ElasticIndexTemplateSettingsBuilder(this);
		elasticIndexTemplateSettingsBuilder.numberOfShards(numOfShards);
		return elasticIndexTemplateSettingsBuilder;
	}
	
	/**
	 * 通过MappingBuilder逐项配置Mapping
	 *
	 * @param dynamic
	 * @return
	 */
	public ElasticIndexTemplateMappingBuilder mappings(Dynamic dynamic) {
		this.mappingBuilder = new ElasticIndexTemplateMappingBuilder(this, dynamic);
		return mappingBuilder;
	}
	
	/**
	 * 执行创建或者更新Index Template
	 */
	public boolean create() {
		PutIndexTemplateRequestBuilder builder = client.admin().indices()
				.preparePutTemplate(name)
				.setPatterns(patterns)
				.setOrder(order)
				.setVersion(version)
				.addMapping(ElasticUtils.ONLY_TYPE, mappingBuilder.build());
		if (settings != null) {
			builder.setSettings((org.elasticsearch.common.settings.Settings) ReflectionUtils.invokeMethod(settings, "build"));
		}
		AcknowledgedResponse response = builder.get();
		return response.isAcknowledged();
	}
}
