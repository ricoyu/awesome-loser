package com.loserico.search.enums.cluster;

/**
 * Enable or disable allocation for specific kinds of shards
 * <p>
 * Copyright: Copyright (c) 2021-04-18 8:22
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum AllocationEnable {
	
	/**
	 * (default) Allows shard allocation for all kinds of shards
	 */
	ALL("all"),
	
	/**
	 * Allows shard allocation only for primary shards
	 */
	PRIMARIES("primaries"),
	
	/**
	 * Allows shard allocation only for primary shards for new indices
	 */
	NEW_PRIMARIES("new_primaries"),
	
	/**
	 * No shard allocations of any kind are allowed for any indices
	 */
	NONE("none");
	
	private String enable;
	
	private AllocationEnable(String enable) {
		this.enable = enable;	
	}
	
	@Override
	public String toString() {
		return enable;
	}
}
