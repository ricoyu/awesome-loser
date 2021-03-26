package com.loserico.searchlegacy.factory;

/**
 * QueryBuilder工厂
 * <p>
 * Copyright: (C), 2021-03-26 11:37
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface QueryBuilderFactory<T> {
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public boolean supports(Object value);
	
	public T build();
}
