package com.loserico.common.lang.resource;

/**
 * <p>
 * Copyright: (C), 2021-01-21 14:13
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface YamlOps {
	
	public boolean exists();
	
	public Integer getInt(String path);
	
	public Integer getInt(String path, Integer defaultValue);
	
	public String getString(String path);
	
	public String getString(String path, String defaultValue);
}
