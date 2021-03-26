package com.loserico.searchlegacy.builder;

import org.elasticsearch.common.settings.Settings.Builder;

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
	
	private org.elasticsearch.common.settings.Settings build() {
		Builder builder = org.elasticsearch.common.settings.Settings.builder()
				.put("number_of_shards", numberOfShards)
				.put("number_of_replicas", numberOfReplicas);
		return builder.build();
	}
	
}
