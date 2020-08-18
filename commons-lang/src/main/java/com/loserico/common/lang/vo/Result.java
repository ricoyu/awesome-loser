package com.loserico.common.lang.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * REST通用输出结果封装
 * <p>
 * Copyright: Copyright (c) 2020-08-17 16:54
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Slf4j
public class Result {
	
	/**
	 * 请求接口状态码
	 */
	private String code = "200";

	/**
	 * message表示在API调用失败的情况下详细的错误信息, 这个信息可以由客户端直接呈现给用户
	 * 调用成功则固定为OK；
	 */
	private Object message = "OK";
	
	/**
	 * 输出的调试信息
	 */
	private Object debugMessage;
	
	/**
	 * 返回的数据
	 */
	private Object data;
	
	public <K, V> Result put(K key, V value) {
		if (data instanceof Map) {
			Map<K, V> map = (Map<K, V>)data;
			map.put(key, value);
		} else {
			log.warn("data is not a Map, cannot call put(k, v)");
		}
		return this;
	}

}
