package com.loserico.mongo.dao;

import com.loserico.common.lang.vo.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 实体类的一些CRUD操作
 * <p>
 * Copyright: (C), 2020-08-20 10:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface EntityOperations {
	
	/**
	 * 如果ID不为null，则调用persist， 否则调用merge
	 *
	 * @param entity
	 * @return 一个新的T对象
	 */
	public <T> T save(T entity);
	
	/**
	 * List中的元素如果ID有值，则调用merge，否则调用persist
	 * @param entities
	 * @return 一个新的List<T>
	 */
	public <T> List<T> save(List<T> entities);
	
	/**
	 * List中的元素如果ID有值，则调用merge，否则调用persist
	 * @param entities
	 * @return 一个新的List<T>
	 */
	public <T> List<T> save(Set<T> entities);
	
	/**
	 * 删除
	 *
	 * @param entity
	 */
	public <T> void delete(T entity);
	
	/**
	 * 删除
	 *
	 * @param entities
	 * @return int 删除的记录数
	 */
	public <T> int delete(List<T> entities);
	
	/**
	 * 根据主键删除
	 *
	 * @param entityClass
	 * @param id 
	 * @return int 删除的记录数
	 */
	public <T, PK extends Serializable> int deleteByPk(Class<T> entityClass, PK id);
	
	/**
	 * 根据主键删除
	 * @param entityClass
	 * @param ids
	 * @param <T>
	 * @param <PK>
	 *     
	 * @return int 删除的记录数
	 */
	public <T, PK extends Serializable> int deleteByPk(Class<T> entityClass, List<PK> ids);
	
	/**
	 * 删除所有
	 * @param entityClass
	 * @param <T>
	 * @param <PK>
	 * @return int 删除总数
	 */
	public <T, PK extends Serializable> int deleteAll(Class<T> entityClass);
	
	/**
	 * 根据主键查找, 找不到则返回null
	 * @param entityClass
	 * @param id
	 * @param <T>
	 * @param <PK>
	 * @return
	 */
	public <T, PK extends Serializable> T get(Class<T> entityClass, PK id);
	
	/**
	 * 根据主键列表查找，返回bean本身，找不到则返回null
	 * @param entityClass
	 * @param ids
	 * @param <T>
	 * @param <PK>
	 * @return List<T>
	 */
	public <T, PK extends Serializable> List<T> getMulti(Class<T> entityClass, PK... ids);
	
	/**
	 * 根据主键列表查找，返回bean本身，找不到则返回null
	 * @param entityClass
	 * @param ids
	 * @param <T>
	 * @param <PK>
	 * @return List<T>
	 */
	public <T, PK extends Serializable> List<T> getMulti(Class<T> entityClass, List<PK> ids);
	
	/**
	 * 根据主键查找，返回bean本身，找不到则抛<code>EntityNotFoundException</code>
	 * @param entityClass
	 * @param id
	 * @param <T>
	 * @param <PK>
	 * @return T
	 */
	public <T, PK extends Serializable> T ensureEntityExists(Class<T> entityClass, PK id);
	
	/**
	 * 查找所有记录
	 *
	 * @param entityClass
	 * @return List<T>
	 */
	public <T> List<T> findAll(Class<T> entityClass);
	
	/**
	 * 查找所有记录, 同时支持排序
	 * 
	 * @param entityClass
	 * @param page
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> findAll(Class<T> entityClass, Page page);
	
}
