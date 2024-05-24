package com.loserico.common.lang.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Result {

	private static final Logger log = LoggerFactory.getLogger(Result.class);
	
	/**
	 * 请求接口状态码, 0表示成功
	 */
	private String code = "0";
	
	/**
	 * success error
	 */
	private String status;

	/**
	 * message表示在API调用失败的情况下详细的错误信息, 这个信息可以由客户端直接呈现给用户
	 * 调用成功则固定为OK；
	 * 数据校验失败时的验证错误页放到这个字段里面
	 */
	private Object message = "OK";
	
	/**
	 * 输出的调试信息
	 */
	//private Object debugMessage;
	
	/**
	 * 返回的数据
	 */
	private Object data;
	
	/**
	 * 分页对象, 如果没有分页, 这个字段不会输出到json串中
	 */
	private Page page;
	
	public <K, V> Result put(K key, V value) {
		if (data instanceof Map) {
			Map<K, V> map = (Map<K, V>)data;
			map.put(key, value);
		} else {
			log.warn("data is not a Map, cannot call put(k, v)");
		}
		return this;
	}

	public Object getData() {
		return this.data;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
