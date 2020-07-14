package com.loserico.sharding.enumeration;

import lombok.Getter;

/**
 * 多数据源错误异常枚举类
 * <p>
 * Copyright: Copyright (c) 2020-02-14 18:39
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Getter
public enum RoutingErrors {
	
	ROUTINGFIELD_ARGS_ISNULL(0, "多数据源路由键为空"),
	
	LOADING_STATEGY_UN_MATCH(1, "路由组件加载和配置文件不匹配"),
	
	FORMAT_TABLE_SUFFIX_ERROR(2, "格式化表后缀异常"),
	
	NO_ROUTING_FIELD(3, "入参中不包含路由字段");
	
	private Integer code;
	
	private String msg;
	
	private RoutingErrors(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
