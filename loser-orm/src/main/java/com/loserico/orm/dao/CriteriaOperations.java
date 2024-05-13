package com.loserico.orm.dao;

import com.loserico.common.lang.vo.Page;
import com.loserico.orm.predicate.Predicate;
import com.loserico.orm.predicate.Querys;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface CriteriaOperations {

	/**
	 * 根据某个属性查找
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> List<T> find(Class<T> entityClass, String propertyName, Object value);

	public <T> List<T> find(Class<T> entityClass, Predicate predicate);

	public <T> List<T> find(Class<T> entityClass, Predicate... predicates);

	<T> List<T> find(Class<T> entityClass, Querys.QueryBuilder queryBuilder);

	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 * @param <T>
	 */
	public <T> T findOne(Class<T> entityClass, String propertyName, Object value);

	public <T> T findOne(Class<T> entityClass, Predicate... predicates);

	/**
	 * 数字字段范围查询
	 * @param entityClass
	 * @param propertyName
	 * @param begin
	 * @param end
	 * @return List<T>
	 * @param <T>
	 */
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, Long begin, Long end);
	
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, Long begin, Long end, Page page);
	
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end);

	/**
	 * 根据属性在给定值列表中来获取，可以指定是否包含软删除的对象
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return List<T>
	 */
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value);
	

	public <T, E> List<T> findIn(Class<T> entityClass, String propertyName, E[] value);


	/**
	 * propertyName 对应字段是null对象
	 * @param entityClass
	 * @param propertyName
	 * @return
	 */
	public <T> List<T> findIsNull(Class<T> entityClass, String propertyName);
	
	/**
	 * 检查有对应entity是否存在
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value);
	
	/**
	 * 根据属性查找，返回唯一一个对象，如果找到多个，取第一个，如果找不到则抛出 EntityNotFoundException
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return T
	 */
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value) throws EntityNotFoundException;

	public <T> int deleteBy(Class<T> entityClass, Predicate... predicates);
	
//	public <T> int deleteByProperties(Class<T> entityClass, List<Predicate> predicates);
	
	/**
	 * 删除属性值在指定列表里面的所有对象
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Collection<?> value);
	
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Object[] values);

	public <T> List<T> leftJoinFetch(Class<T> entityClass, Predicate predicate, String... attributeNames);

	public <T> T leftJoinFetchSingleResult(Class<T> entityClass, Predicate predicate, String... attributeNames);
	
	public <T> List<T> leftJoinFetch(Class<T> entityClass, List<Predicate> predicates, String... attributeNames);

}
