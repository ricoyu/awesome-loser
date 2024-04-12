package com.loserico.orm.dao;

import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.Page;
import com.loserico.orm.criteria.JPACriteriaQuery;
import com.loserico.orm.predicate.Predicate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AdvCriteriaOperations {

	public <T> List<T> find(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);

	public <T> List<T> find(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted, Page page);

	/**
	 * 根据某个属性查找，支持排序
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> List<T> find(Class<T> entityClass, String propertyName, Object value, OrderBean... orders);

	public <T> List<T> find(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted, OrderBean... orders);

	/**
	 * 根据某个属性查找，支持排序,支持过滤软删除记录
	 * 
	 * @param propertyName
	 * @param value
	 * @param page 分页参数
	 * @return
	 */
	public <T> List<T> find(Class<T> entityClass, String propertyName, Object value, Page page);

	public <T> List<T> find(Class<T> entityClass, Predicate predicate, OrderBean... orders);

	public <T> List<T> find(Class<T> entityClass, Predicate predicate, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> find(Class<T> entityClass, Predicate predicate,  boolean includeDeleted, Page page);

	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个, 可以指定是否包含逻辑删除字段deleted=true的记录
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 * @param <T>
	 */
	public <T> T findOne(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);

	public <T> T findOne(Class<T> entityClass, String propertyName, Object value, OrderBean... orders);

	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个,可以指定是否包含软删除的记录,找不到则返回null
	 *
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param includeDeleted 是否包含软删除的记录
	 * @return
	 */
	public <T> T findOne(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, Long begin, Long end, Page page);
	
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, boolean includeDeleted);

	/**
	 * 日期的between
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param begin
	 * @param end
	 * @return
	 */
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, OrderBean... orders);
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, Page page);
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, boolean includeDeleted, Page page);

	/**
	 * 根据属性在给定值列表中来获取，可以指定是否包含软删除的对象
	 * 如果values列表太长, 考虑并发方式分批获取数据? 因为MySQL会限制in语句中条件的数量
	 * TODO
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param values
	 * @param includeDeleted
	 * @return List<T>
	 */
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> values, boolean includeDeleted);
	
	public <T, E> List<T> findIn(Class<T> entityClass, String propertyName, E[] value, boolean includeDeleted);

	/**
	 * 给定一个值列表，查找某个属性值在这个列表里面的结果集，SQL的IN语法
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value, OrderBean... orders);
	
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value, Page page);
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value, boolean includeDeleted, Page page);
	
	public <T> List<T> findIsNull(Class<T> entityClass, String propertyName, boolean includeDeleted);
	
	/**
	 * 检查有对应entity是否存在
	 * 不包含逻辑删除的数据
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param includeDeleted
	 * @return
	 */
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);

	/**
	 * 根据属性查找，返回唯一一个对象，如果找到多个，取第一个，如果找不到则抛出 EntityNotFoundException
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return T
	 */
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value) throws EntityNotFoundException;
	
	/**
	 * 根据属性查找，返回唯一一个对象，如果找到多个，取第一个，如果找不到则抛出 EntityNotFoundException
	 * 可以指定是否包含逻辑删除的记录
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return T
	 */	
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted) throws EntityNotFoundException;
	
	public <T> JPACriteriaQuery<T> createJPACriteriaQuery(Class<T> entityClass, List<Predicate> predicates, OrderBean... orders);

//	public <T> int deleteByProperty(Class<T> entityClass, String propertyName, Object propertyValue);

	public <T> int deleteBy(Class<T> entityClass, Predicate... predicates);
	
//	public <T> int deleteByProperties(Class<T> entityClass, List<Predicate> predicates);
	

	public <T> T leftJoinFetchSingleResult(Class<T> entityClass, Predicate predicate, List<OrderBean> orders, String... attributeNames);

}
