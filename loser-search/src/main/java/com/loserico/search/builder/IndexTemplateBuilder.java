package com.loserico.search.builder;

import com.loserico.search.ElasticUtils;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

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
public final class IndexTemplateBuilder {
	
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
	private MappingBuilder mappingBuilder;
	
	private IndexTemplateBuilder() {
	}
	
	/**
	 * 创建一个IndexTemplateBuilder, 并指定Index Template的名字
	 *
	 * @param name
	 * @return
	 */
	public static IndexTemplateBuilder newInstance(TransportClient client, String name) {
		IndexTemplateBuilder builder = new IndexTemplateBuilder();
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
	public IndexTemplateBuilder patterns(String... patterns) {
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
	public IndexTemplateBuilder order(int order) {
		this.order = order;
		return this;
	}
	
	public IndexTemplateBuilder version(Integer version) {
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
	public IndexTemplateBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	/**
	 * 通过MappingBuilder逐项配置Mapping
	 *
	 * @param mappingBuilder
	 * @return
	 */
	public IndexTemplateBuilder mappings(MappingBuilder mappingBuilder) {
		this.mappingBuilder = mappingBuilder;
		return this;
	}
	
	/**
	 * 执行创建或者更新Index Template
	 */
	public boolean execute() {
		AcknowledgedResponse response = client.admin().indices()
				.preparePutTemplate(name)
				.setPatterns(patterns)
				.setOrder(order)
				.setVersion(version)
				.setSettings(settings)
				.addMapping(ElasticUtils.ONLY_TYPE, mappingBuilder.build())
				.get();
		return response.isAcknowledged();
	}
}
