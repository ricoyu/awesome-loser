package com.loserico.mongo.dao;

import com.loserico.common.lang.vo.Page;

import java.util.List;

/**
 * 通过Mongo Shell Script 操作, 支持变量占位符 #{}, 如{"name": "#{name}"}
 * <p>
 * Copyright: (C), 2020-09-14 16:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface ScriptOperations {
	
	/**
	 * 通过Mongo Shell Script 查询
	 * @param json Mongo Shell 脚本
	 * @param entityClass 实体类class
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, Class<T> entityClass);
	
	/**
	 * 通过Mongo Shell Script 查询
	 * @param json Mongo Shell 脚本
	 * @param entityClass 实体类class
	 * @param param 参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, Class<T> entityClass, Object param);
	
	/**
	 * 通过Mongo Shell Script 查询
	 * @param json Mongo Shell 脚本
	 * @param page 分页参数
	 * @param entityClass 实体类class
	 * @param param 参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, Page page, Class<T> entityClass, Object param);
	
	/**
	 * 通过Mongo Shell Script 查询
	 * @param json
	 * @param sortJson
	 * @param entityClass
	 * @param <T>
	 * @return
	 */
	public <T> List<T> queryForList(String json, String sortJson, Class<T> entityClass);
	
	/**
	 * 通过Mongo Shell Script 查询
	 * @param json Mongo Shell 脚本
	 * @param sortJson Mongo Shell 排序脚本
	 * @param entityClass 实体类class
	 * @param param 参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, String sortJson, Class<T> entityClass, Object param);
	
	/**
	 * 通过Mongo Shell Script 聚合查询
	 * @param script
	 * @param entityClass
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> aggregationQuery(String script, Class<T> entityClass);
}
