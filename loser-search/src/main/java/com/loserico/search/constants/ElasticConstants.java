package com.loserico.search.constants;

/**
 * <p>
 * Copyright: (C), 2021-03-25 15:38
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticConstants {
	
	/**
	 * 唯一的一个type是_doc
	 */
	public static final String ONLY_TYPE = "_doc";
	
	/**
	 * 聚合时候, 如果需要返回总命中数, 可以把总命中数放到ThreadLocal里面, 按需获取
	 */
	public static final String TOTAL_HITS = "totalHits";
}
