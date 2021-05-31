package com.loserico.search.support;

import com.loserico.search.annotation.Index;

/**
 * <p>
 * Copyright: (C), 2021-05-06 15:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class IndexSupport {
	
	/**
	 * 从@Index注解上拿索引名
	 * @param entityClass 
	 * @return String 索引名
	 */
	public static String indexName(Class entityClass) {
		if (entityClass == null) {
			return null;
		}
		
		Index index = (Index)entityClass.getAnnotation(Index.class);
		if (index == null) {
			return null;
		}
		
		return index.value().trim();
	}
}
