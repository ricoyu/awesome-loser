package com.loserico.search.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-02-23 21:26
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Builder
public class ElasticPage<T> implements Serializable {
	
	private static final long serialVersionUID = 4426577582458914214L;
	
	/**
	 * 这是查询返回的数据部分
	 */
	private List<T> results;
	
	/**
	 * 这是给下一次Search After查询用的sort
	 */
	private Object[] sort;
	
	/**
	 * 返回一个空的PageResult, results是一个空的不可变List, sort是一个长度为0的数组
	 * @return PageResult
	 */
	public static ElasticPage emptyResult() {
		return ElasticPage.builder()
				.results(Collections.emptyList())
				.sort(new Object[0])
				.build();
	}
}
