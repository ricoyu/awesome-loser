package com.loserico.search.vo;

import com.loserico.common.lang.vo.Page;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-08-10 12:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Builder
public class ElasticScroll<T> extends Page {
	
	
	/**
	 * 这是查询返回的数据部分
	 */
	private List<T> results;
	
	private String scrollId;
	
	public static ElasticScroll emptyResult() {
		return ElasticScroll.builder()
				.results(Collections.emptyList())
				.build();
	}
}
