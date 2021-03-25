package com.loserico.search.builder;

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
public final class Settings {
	
	/**
	 * number_of_shards
	 */
	private int numberOfShards = 1;
	
	/**
	 * number_of_replicas
	 */
	private int numberOfReplicas = 0;
	
	/**
	 * index.default_pipeline
	 * <p>
	 * The default ingest node pipeline for this index.
	 * Index requests will fail if the default pipeline is set and the pipeline does not exist.
	 * The default may be overridden using the pipeline parameter.
	 */
	private String defaultPipeline;
	
	private Settings() {
	}
	
	public static Settings builder() {
		return new Settings();
	}
	
	/**
	 * 设置主分片数, 默认1
	 *
	 * @param numberOfShards
	 * @return SettingsBuilder
	 */
	public Settings numberOfShards(int numberOfShards) {
		this.numberOfShards = numberOfShards;
		return this;
	}
	
	/**
	 * 设置副本数, 默认0
	 *
	 * @param numberOfReplicas
	 * @return SettingsBuilder
	 */
	public Settings numberOfReplicas(int numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
		return this;
	}
	
	/**
	 * 设置这个索引默认的pipeline
	 *
	 * @param defaultPipeline
	 * @return SettingsBuilder
	 */
	public Settings defaultPipeline(String defaultPipeline) {
		this.defaultPipeline = defaultPipeline;
		return this;
	}
	
	private org.elasticsearch.common.settings.Settings build() {
		return org.elasticsearch.common.settings.Settings.builder()
				.put("number_of_shards", numberOfShards)
				.put("number_of_replicas", numberOfReplicas)
				.put("index.default_pipeline", defaultPipeline)
				.build();
	}
	
}
