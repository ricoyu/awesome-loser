package com.loserico.common.lang.io;

/**
 * <p>
 * Copyright: (C), 2021-03-03 14:23
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface Node {
	
	/**
	 * YAML文件key value之间的分隔符
	 */
	public static final String SEPARATOR = ": ";
	
	/**
	 * 
	 * @return
	 */
	public String paddingSpace();
	
	/**
	 * 对应key: value中的key值
	 * @return String
	 */
	public String name();
	
	/**
	 * 对应key: value中的value值
	 * @return
	 */
	public String value();
	
	public String toString();
}
