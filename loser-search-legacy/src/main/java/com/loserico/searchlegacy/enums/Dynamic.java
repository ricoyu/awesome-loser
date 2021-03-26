package com.loserico.searchlegacy.enums;

/**
 * 控制文档的dynamic属性
 * <p>
 * Copyright: Copyright (c) 2020-12-28 8:56
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum Dynamic {
	
	/**
	 * 一旦有新增字段的文档写入, Mapping也同时被更新
	 */
	TRUE("true"),
	
	/**
	 * Mapping不会被更新, 新增字段的数据无法被索引, 但是信息会出现在_source中
	 */
	FALSE("false"),
	
	/**
	 * 有新增字段则文档写入失败
	 */
	STRICT("strict");
	
	private String property;
	
	private Dynamic(String property) {
		this.property = property;
	}
	
	
	@Override
	public String toString() {
		return this.property;
	}
}
