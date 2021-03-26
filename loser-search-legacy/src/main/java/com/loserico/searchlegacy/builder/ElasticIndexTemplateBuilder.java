package com.loserico.searchlegacy.builder;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequestBuilder;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Objects;

import static com.loserico.searchlegacy.constants.ElasticConstants.ONLY_TYPE;

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
	
	private String pattern;
	
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
	private ElasticMappingBuilder mappingBuilder;
	
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
	 * @param pattern
	 * @return
	 */
	public ElasticIndexTemplateBuilder patterns(String pattern) {
		Objects.requireNonNull(pattern, "pattern can not be null");
		this.pattern = pattern;
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
	
	/*public ElasticIndexTemplateBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}*/
	
	/**
	 * 通过MappingBuilder逐项配置Mapping
	 *
	 * @param mappingBuilder
	 * @return
	 */
	public ElasticIndexTemplateBuilder mappings(ElasticMappingBuilder mappingBuilder) {
		this.mappingBuilder = mappingBuilder;
		return this;
	}
	
	/**
	 * 执行创建或者更新Index Template
	 */
	public boolean execute() {
		PutIndexTemplateRequestBuilder builder = client.admin().indices()
				.preparePutTemplate(name)
				.setTemplate(pattern)
				.setOrder(order)
				.setVersion(version)
				.addMapping(ONLY_TYPE, mappingBuilder.build());
		if (settings != null) {
			builder.setSettings((org.elasticsearch.common.settings.Settings) ReflectionUtils.invokeMethod(settings, "build"));
		}
		AcknowledgedResponse response = builder.get();
		return response.isAcknowledged();
	}
}
