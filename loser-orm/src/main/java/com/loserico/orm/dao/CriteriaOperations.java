package com.loserico.orm.dao;

import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.Page;
import com.loserico.orm.criteria.JPACriteriaQuery;
import com.loserico.orm.predicate.Predicate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CriteriaOperations {

	/**
	 * 根据某个属性查找，可以指定是否包含软删除的记录
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);
	
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

	/**
	 * 根据某个属性查找，支持排序
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, OrderBean... orders);

	/**
	 * 根据某个属性查找，支持排序,支持过滤软删除记录
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, Page page);
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted, Page page);

	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate);
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, boolean includeDeleted);

	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, OrderBean... orders);
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, Page page);
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, boolean includeDeleted, Page page);
	
	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个,可以指定是否包含软删除的记录,找不到则返回null
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param includeDeleted 是否包含软删除的记录
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted, OrderBean... orders);
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value, OrderBean... orders);

	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);
	
	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个,可以指定是否包含软删除的记录,找不到则返回null
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param includeDeleted 是否包含软删除的记录
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);
	
	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return Optional
	 */	
	public <T> Optional<T> findOne(Class<T> entityClass, String propertyName, Object value);
	
	/**
	 * 根据属性查找，返回一个对象，如果找到多个，取第一个
	 * 
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return Optional
	 */	
	public <T> Optional<T> findOne(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted);

	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates);
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted);
	
	/**
	 * 根据多个属性查找
	 * 
	 * @return
	 */
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, OrderBean... orders);
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, Page page);
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted, Page page);

	/**
	 * 根据多个属性查找，找不到返回null
	 * 
	 * @param entityClass
	 * @param predicates
	 * @return
	 */
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates);
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted);

	/**
	 * 根据多个属性和排序规则查找，找不到返回null
	 * 
	 * @param entityClass
	 * @param predicates
	 * @param orders
	 * @return
	 */
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates, OrderBean... orders);
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted, OrderBean... orders);

	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end);
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
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return List<T>
	 */
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value);
	
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
	
	public <T, E> List<T> findIn(Class<T> entityClass, String propertyName, E[] value);
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
	
	/**
	 * propertyName 对应字段是null对象
	 * @param entityClass
	 * @param propertyName
	 * @return
	 */
	public <T> List<T> findIsNull(Class<T> entityClass, String propertyName);
	
	public <T> List<T> findIsNull(Class<T> entityClass, String propertyName, boolean includeDeleted);
	
	/**
	 * 检查有对应entity是否存在
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value);
	
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

	public <T> int deleteByProperty(Class<T> entityClass, String propertyName, Object propertyValue);

	public <T> int deleteByProperties(Class<T> entityClass, List<Predicate> predicates);
	
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
	
	public <T> T leftJoinFetchSingleResult(Class<T> entityClass, Predicate predicate, List<OrderBean> orders, String... attributeNames);

	public <T> List<T> leftJoinFetch(Class<T> entityClass, List<Predicate> predicates, String... attributeNames);

}
