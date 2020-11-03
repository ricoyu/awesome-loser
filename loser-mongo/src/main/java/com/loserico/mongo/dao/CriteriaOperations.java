package com.loserico.mongo.dao;

import com.loserico.common.lang.exception.EntityNotFoundException;
import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.Page;

import java.util.Collection;
import java.util.List;

/**
 * 根据属性条件查询
 * <p>
 * Copyright: (C), 2020-08-20 10:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface CriteriaOperations {
	
	/**
	 * 根据某个属性查找
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param <T>
	 * @return List<T>
	 */
	
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);
	
	
	/**
	 * 根据某个属性查找
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param <T>
	 * @return List<T>
	 */
	
	public <T> T findOneByProperty(Class<T> entityClass, String propertyName, Object value);
	
	/**
	 * 根据某个属性查找, 支持排序
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param orders
	 * @param <T>
	 * @return
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, OrderBean... orders);
	
	/**
	 * 根据某个属性查找, 支持分页
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param page
	 * @param <T>
	 * @return
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, Page page);
	
	/**
	 * 检查有对应entity是否存在
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return boolean
	 */
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value);
	
	/**
	 * 检查有对应entity是否存在, 可以指定是否包含软删除的记录
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param includeDeleted
	 * @return boolean
	 */
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);
	
	/**
	 * 根据属性查找, 返回唯一一个对象, 如果找到多个, 取第一个; 如果找不到则抛出 EntityNotFoundException
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param <T>
	 * @return T
	 * @throws EntityNotFoundException
	 */
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value) throws EntityNotFoundException;
	
	/**
	 * 根据属性查找, 返回唯一一个对象, 如果找到多个, 取第一个; 如果找不到则抛出 EntityNotFoundException. 可以指定是否包含软删除的记录
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param includeDeleted
	 * @param <T>
	 * @return
	 * @throws EntityNotFoundException
	 */
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted) throws EntityNotFoundException;
	
	/**
	 * 根据属性删除对象
	 * @param entityClass
	 * @param propertyName
	 * @param propertyValue
	 * @param <T>
	 * @return int 删除的数量
	 */
	public <T> int deleteByProperty(Class<T> entityClass, String propertyName, Object propertyValue);
	
	/**
	 * 删除属性值在指定列表里面的所有对象
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return 删除的数量
	 */
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Collection<?> value);
	
	/**
	 * 删除属性值在指定列表里面的所有对象
	 * @param entityClass
	 * @param propertyName
	 * @param values
	 * @param <T>
	 * @return 删除的数量
	 */
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Object... values);
	
	/**
	 * 查找匹配的记录数; 只会用entity中非null字段去匹配, 如果字段是null则不管
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int count(T entity);
	
}
