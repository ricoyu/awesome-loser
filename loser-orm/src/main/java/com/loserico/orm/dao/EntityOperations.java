package com.loserico.orm.dao;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 针对实体类的简单CRUD操作
 * 
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-01-29 20:36
 * @version 1.0
 *
 */
public interface EntityOperations {

	/**
	 * Make an instance managed and persistent.
	 * 
	 * @param entity
	 */
	public <T> void persist(T entity);

	public <T> void persist(List<T> entities);

	/**
	 * merge返回的是一个受当前Persistence Context管理的新对象
	 * 
	 * @param entity
	 * @return
	 */
	public <T> T merge(T entity);

	/**
	 * merge返回的是一个受当前Persistence Context管理的新对象
	 * 
	 * @param entity
	 * @return List<T>
	 */
	public <T> List<T> merge(List<T> entities);

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
	 * @param entity
	 */
	public <T> void delete(List<T> entities);

	/**
	 * 根据主键删除
	 * 
	 * @param entityClass
	 * @param id
	 */
	public <T, PK extends Serializable> void deleteByPK(Class<T> entityClass, PK id);
	
	public <T, PK extends Serializable> void deleteByPK(Class<T> entityClass, List<PK> ids);

	/**
	 * 跟Hibernate的get方法同语义，根据主键查找，返回bean本身，找不到则返回null
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T, PK extends Serializable> T get(Class<T> entityClass, PK id);

	/**
	 * 跟Hibernate的get方法同语义，根据主键列表查找，返回bean本身，找不到则返回null
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public <T, PK extends Serializable> List<T> getMulti(Class<T> entityClass, PK... ids);

	/**
	 * 跟Hibernate的get方法同语义，根据主键列表查找，返回bean本身，找不到则返回null
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */	
	public <T, PK extends Serializable> List<T> getMulti(Class<T> entityClass, List<PK> ids);

	/**
	 * 根据主键查找，但不包括逻辑删除的(deleted=true)对象
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public <T, PK extends Serializable> T find(Class<T> entityClass, PK id);
	
	/**
	 * 根据主键查找对象
	 * @param entityClass
	 * @param id
	 * @return Optional<T>
	 */
	public <T, PK extends Serializable> Optional<T> findOne(Class<T> entityClass, PK id);

	/**
	 * 跟Hibernate的load方法同语义，延迟加载Entity，Entity必须存在，不存在访问Entity会报错。
	 * 
	 * @param entityClass
	 * @param id
	 * @return T
	 */
	public <T, PK extends Serializable> T load(Class<T> entityClass, PK id);

	/**
	 * 跟Hibernate的get方法同语义，根据主键查找，返回bean本身，找不到则抛<code>EntityNotFoundException</code>
	 * 
	 * @param entityClass
	 * @param id
	 * @return T
	 * @throws EntityNotFoundException
	 */
	public <T, PK extends Serializable> T ensureEntityExists(Class<T> entityClass, PK id);

	/**
	 * 跟Hibernate的get方法同语义，根据主键列表查找，返回的List中包含的是Entity类而不是其代理类<br/>
	 * 任意一个ID没有对应Entity都会抛<code>EntityNotFoundException</code>
	 * 
	 * @param entityClass
	 * @param ids
	 * @return List<T>
	 * @throws EntityNotFoundException
	 * @on
	 */	
	@SuppressWarnings("unchecked")
	public <T, PK extends Serializable> List<T> ensureMultiEntityExists(Class<T> entityClass, PK... ids);
	
	/**
	 * 跟Hibernate的get方法同语义，根据主键列表查找，返回的List中包含的是Entity类而不是其代理类<br/>
	 * 任意一个ID没有对应Entity都会抛<code>EntityNotFoundException</code>
	 * 
	 * @param entityClass
	 * @param ids
	 * @return List<T>
	 * @throws EntityNotFoundException
	 * @on
	 */	
	public <T, PK extends Serializable> List<T> ensureMultiEntityExists(Class<T> entityClass, List<PK> ids);

	/**
	 * 查找所有记录
	 * 
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> findAll(Class<T> entityClass);

	/**
	 * 将persist状态的entity设为detached状态
	 * 
	 * @param entity
	 */
	public <T> void detach(T entity);

	/**
	 * 将persist状态的entity设为detached状态
	 * 
	 * @param entities
	 */
	public <T> void detach(List<T> entities);

	/**
	 * Synchronize the persistence context to the underlying database.
	 */
	public void flush();

}
