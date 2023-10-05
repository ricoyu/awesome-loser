package com.loserico.orm.dao;

import com.loserico.common.lang.transformer.ValueHandlerFactory;
import com.loserico.common.lang.utils.ArrayTypes;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.common.lang.utils.SqlUtils;
import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.Page;
import com.loserico.orm.criteria.JPACriteriaQuery;
import com.loserico.orm.exception.EntityOperationException;
import com.loserico.orm.exception.JPQLException;
import com.loserico.orm.exception.PersistenceException;
import com.loserico.orm.exception.RawSQLQueryException;
import com.loserico.orm.exception.SQLCountQueryException;
import com.loserico.orm.exception.SQLQueryException;
import com.loserico.orm.predicate.Predicate;
import com.loserico.orm.transformer.ResultTransformerFactory;
import com.loserico.orm.utils.Defaults;
import com.loserico.orm.utils.HashUtils;
import com.loserico.orm.utils.JacksonUtils;
import com.loserico.orm.utils.PrimitiveUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.text.MessageFormat.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 基于JPA 2.1封装的通用DAO
 *
 * @author Loser
 * @since Mar 6, 2016
 */
@Repository
public class JpaDao implements JPQLOperations, SQLOperations, CriteriaOperations, EntityOperations, InitializingBean {
	
	private static Logger log = LoggerFactory.getLogger(JpaDao.class);
	
	private static final String IS_COUNT_QUERY = "isCountQuery";
	
	private static final String HINT_QUERY_CACHE = "org.hibernate.cacheable";
	
	private static final ConcurrentMap<String, ArrayTypes> ARRAY_TYPE_MAP = new ConcurrentHashMap<>();
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Value("${hibernate.query.mode:loose}")
	private String hibernateQueryMode = "loose";
	
	@Value("${hibernate.query.cache:false}")
	private boolean hibernateUseQueryCache = false;
	
	@Value("${hibernate.jdbc.batch_size:100}")
	private int batchSize = 0;
	
	private boolean useDefaultOrder = false;
	
	/**
	 * 如果类的某个属性是enum类型，并且需要根据这个enum类型的某个属性来和数据库列值匹配，那么要知名这个属性的名字
	 */
	private Set<String> enumLookupProperties = new HashSet<>();
	
	/**
	 * 默认会根据CREATE_TIME倒序排
	 */
	private OrderBean order = new OrderBean("CREATE_TIME", OrderBean.ORDER_BY.DESC);
	
	/**
	 * 配置JpaDao的时候指定context-className
	 */
	private Map<String, String> contextClasses = new HashMap<>();
	
	/**
	 * 根据contextClasses找对应的Class对象在namedSqlQuery中会把classMap中的key/value对put到VelocityContext中
	 * 这样在SQL里面就可以用了, 示例如下:
	 * <pre>{@code
	 *  #if($StringUtils.isNotBlank($userName))
	 *     AND u.username LIKE :userName
	 *  #end
	 * }</pre>
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Class> classMap = new HashMap<>();
	
	static {
		ARRAY_TYPE_MAP.put(ArrayTypes.LONG.getClassName(), ArrayTypes.LONG);
		ARRAY_TYPE_MAP.put(ArrayTypes.LONG_WRAPPER.getClassName(), ArrayTypes.LONG_WRAPPER);
		ARRAY_TYPE_MAP.put(ArrayTypes.INTEGER.getClassName(), ArrayTypes.INTEGER);
		ARRAY_TYPE_MAP.put(ArrayTypes.INTEGER_WRAPPER.getClassName(), ArrayTypes.INTEGER_WRAPPER);
		ARRAY_TYPE_MAP.put(ArrayTypes.STRING.getClassName(), ArrayTypes.STRING);
		ARRAY_TYPE_MAP.put(ArrayTypes.DOUBLE.getClassName(), ArrayTypes.DOUBLE);
		ARRAY_TYPE_MAP.put(ArrayTypes.DOUBLE_WRAPPER.getClassName(), ArrayTypes.DOUBLE_WRAPPER);
		ARRAY_TYPE_MAP.put(ArrayTypes.FLOAT.getClassName(), ArrayTypes.FLOAT);
		ARRAY_TYPE_MAP.put(ArrayTypes.FLOAT_WRAPPER.getClassName(), ArrayTypes.FLOAT_WRAPPER);
		
		Properties properties = new Properties();
		properties.setProperty("userdirective",
				"com.loserico.orm.directive.IfNotNull," +
						"com.loserico.orm.directive.IfNull," +
						"com.loserico.orm.directive.Count," +
						"com.loserico.orm.directive.Between," +
						"com.loserico.orm.directive.OmitForCount," +
						"com.loserico.orm.directive.IfPresent");
		properties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		properties.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		//初始化运行时引擎
		Velocity.init(properties);
	}
	
	/**
	 * Make an instance managed and persistent.
	 *
	 * @param entity
	 */
	@Override
	public <T> void persist(T entity) {
		Objects.requireNonNull(entity, "entity cannot be null");
		try {
			entityManager.persist(entity);
		} catch (Throwable e) {
			String msg = MessageFormat.format("Entity: {0}", JacksonUtils.toPrettyJson(entity));
			log.error(msg, e);
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public <T> void persist(List<T> entities) {
		Objects.requireNonNull(entities, "entities cannot be null");
		if (entities.isEmpty()) {
			return;
		}
		try {
			for (int i = 0; i < entities.size(); i++) {
				entityManager.persist(entities.get(i));
			}
		} catch (Throwable e) {
			log.error("", e);
			throw new PersistenceException(e);
		}
	}
	
	/**
	 * merge返回的是一个受当前Persistence Context管理的新对象
	 *
	 * @param entity
	 * @return T
	 */
	@Override
	public <T> T merge(T entity) {
		Objects.requireNonNull(entity, "entity cannot be null");
		try {
			return entityManager.merge(entity);
		} catch (Throwable e) {
			String msg = format("Entity: {0}", JacksonUtils.toPrettyJson(entity));
			log.error(msg, e);
			throw new PersistenceException(e);
		}
	}
	
	/**
	 * merge返回的是一个受当前Persistence Context管理的新对象
	 *
	 * @param entities
	 * @return List<T>
	 */
	@Override
	public <T> List<T> merge(List<T> entities) {
		if (isEmpty(entities)) {
			return emptyList();
		}
		List<T> results = new ArrayList<>();
		for (T entity : entities) {
			Objects.requireNonNull(entity, "entity cannot be null");
			try {
				results.add(entityManager.merge(entity));
			} catch (Throwable e) {
				String msg = format("Entity: {0}", JacksonUtils.toPrettyJson(entity));
				log.error(msg, e);
				throw new PersistenceException(e);
			}
		}
		flush();
		return results;
	}
	
	@Override
	public <T> T save(T entity) {
		Objects.requireNonNull(entity, "entity cannot be null");
		Object id = ReflectionUtils.getFieldValue("id", entity);
		if (id == null) {
			persist(entity);
			return entity;
		} else {
			try {
				return entityManager.merge(entity);
			} catch (Throwable e) {
				String msg = format("Entity: {0}", JacksonUtils.toPrettyJson(entity));
				log.error(msg, e);
				throw new PersistenceException(e);
			}
		}
	}
	
	@Override
	public <T> List<T> save(List<T> entities) {
		if (isEmpty(entities)) {
			return emptyList();
		}
		List<T> results = new ArrayList<>();
		for (int i = 0, length = entities.size(); i < length; i++) {
			results.add(save(entities.get(i)));
			if (i > 0 && (i % batchSize == 0)) {
				flush();
			}
		}
		/*
		 * You may also want to flush and clear the persistence context
		 * after each batch to release memory, otherwise all of the managed
		 * objects remain in the persistence context until it is closed.
		 */
		flush();
		return results;
	}
	
	@Override
	public <T> List<T> save(Set<T> entities) {
		if (isEmpty(entities)) {
			return emptyList();
		}
		List<T> results = new ArrayList<>();
		int i = 0;
		for (T entity : entities) {
			results.add(save(entity));
			i++;
			if (i > 0 && (i % batchSize == 0)) {
				flush();
			}
		}
		/*
		 * You may also want to flush and clear the persistence context
		 * after each batch to release memory, otherwise all of the managed
		 * objects remain in the persistence context until it is closed.
		 */
		flush();
		return results;
	}
	
	/**
	 * 删除
	 *
	 * @param entity
	 */
	@Override
	public <T> void delete(T entity) {
		Objects.requireNonNull(entity, "entity cannot be null");
		try {
			entityManager.remove(entity);
		} catch (Throwable e) {
			log.error("", e);
			throw e;
		}
	}
	
	/**
	 * 删除
	 *
	 * @param entities
	 */
	@Override
	public <T> void delete(List<T> entities) {
		Objects.requireNonNull(entities, "entity cannot be null");
		try {
			for (T t : entities) {
				if (t != null) {
					entityManager.remove(t);
				}
			}
		} catch (Throwable e) {
			log.error("", e);
			throw e;
		}
	}
	
	/**
	 * 根据主键删除
	 *
	 * @param entityClass
	 * @param id
	 */
	@Override
	public <T, PK extends Serializable> void deleteByPK(Class<T> entityClass, PK id) {
		Objects.requireNonNull(id, "id cannot be null");
		T entity = entityManager.getReference(entityClass, id);
		delete(entity);
	}
	
	@Override
	public <T, PK extends Serializable> void deleteByPK(Class<T> entityClass, List<PK> ids) {
		Objects.requireNonNull(ids, "ids cannot be null");
		if (ids.size() == 0) {
			return;
		}
		for (PK pk : ids) {
			deleteByPK(entityClass, pk);
		}
	}
	
	/**
	 * 根据主键查找
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Override
	public <T, PK extends Serializable> T get(Class<T> clazz, PK id) {
		Objects.requireNonNull(id, "id cannot be null");
		log.debug("Try to find " + clazz.getName() + " by id " + id);
		try {
			return entityManager.find(clazz, id);
		} catch (Throwable e) {
			log.error("", e);
			throw new EntityOperationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T, PK extends Serializable> List<T> getMulti(Class<T> clazz, PK... ids) {
		Objects.requireNonNull(ids, "ids cannot be null");
		log.debug("Try to find " + clazz.getName() + " by ids " + ids);
		try {
			Session session = entityManager.unwrap(Session.class);
			return session.byMultipleIds(clazz).multiLoad(ids);
		} catch (Throwable e) {
			log.error("", e);
			throw new EntityOperationException(e);
		}
	}
	
	@Override
	public <T, PK extends Serializable> List<T> getMulti(Class<T> clazz, List<PK> ids) {
		Objects.requireNonNull(ids, "ids cannot be null");
		log.debug("Try to find " + clazz.getName() + " by ids " + ids);
		try {
			Session session = entityManager.unwrap(Session.class);
			return session.byMultipleIds(clazz).multiLoad(ids);
		} catch (Throwable e) {
			log.error("", e);
			throw new EntityOperationException(e);
		}
	}
	
	@Override
	public <T, PK extends Serializable> T find(Class<T> clazz, PK id) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
		Root<T> root = criteriaQuery.from(clazz);
		javax.persistence.criteria.Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id);
		javax.persistence.criteria.Predicate deletedPredicate = criteriaBuilder.equal(root.get("deleted"), false);
		criteriaQuery.select(root)
				.where(idPredicate, deletedPredicate)
				.distinct(true);
		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		List<T> results = query.getResultList();
		if (results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}
	
	@Override
	public <T, PK extends Serializable> Optional<T> findOne(Class<T> clazz, PK id) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
		Root<T> root = criteriaQuery.from(clazz);
		javax.persistence.criteria.Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id);
		criteriaQuery.select(root)
				.where(idPredicate)
				.distinct(true);
		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		List<T> results = query.getResultList();
		if (results.isEmpty()) {
			return Optional.ofNullable(null);
		}
		return Optional.ofNullable(results.get(0));
	}
	
	@Override
	public <T, PK extends Serializable> T load(Class<T> entityClass, PK id) {
		try {
			return entityManager.getReference(entityClass, id);
		} catch (Throwable e) {
			log.error("", e);
			throw new EntityOperationException(e);
		}
	}
	
	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass);
		criteriaQuery.from(entityClass);
		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		return query.getResultList();
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate) {
		return findByProperty(entityClass, predicate, true);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, boolean includeDeleted) {
		Objects.requireNonNull(predicate, "predicate cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicate(predicate);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, OrderBean... orders) {
		return findByProperty(entityClass, predicate, true, orders);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, boolean includeDeleted,
	                                  OrderBean... orders) {
		Objects.requireNonNull(predicate, "predicate cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicate(predicate)
				.addOrders(orders);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, Page page) {
		return findByProperty(entityClass, predicate, true, page);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, Predicate predicate, boolean includeDeleted,
	                                  Page page) {
		Objects.requireNonNull(predicate, "predicate cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicate(predicate);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.setPage(page).list();
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
		return findByProperty(entityClass, propertyName, value, true);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value,
	                                  boolean includeDeleted) {
		Objects.requireNonNull(propertyName, "propertyName cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery =
				JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache);
		if (value == null) {
			jpaCriteriaQuery.isNull(propertyName);
		} else {
			jpaCriteriaQuery.eq(propertyName, value);
		}
		
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, Page page) {
		return findByProperty(entityClass, propertyName, value, true, page);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value,
	                                  boolean includeDeleted, Page page) {
		requireNonNull(propertyName, "propertyName cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery =
				JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache);
		if (value == null) {
			jpaCriteriaQuery.isNull(propertyName);
		} else {
			jpaCriteriaQuery.eq(propertyName, value);
		}
		
		jpaCriteriaQuery.setPage(page);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value,
	                                  OrderBean... orders) {
		return findByProperty(entityClass, propertyName, value, true, orders);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value,
	                                  boolean includeDeleted, OrderBean... orders) {
		Objects.requireNonNull(propertyName, "propertyName cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery =
				JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache);
		if (value == null) {
			jpaCriteriaQuery.isNull(propertyName);
		} else {
			jpaCriteriaQuery.eq(propertyName, value);
		}
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.addOrders(orders).list();
	}
	
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		return findUniqueByProperty(entityClass, propertyName, value, true);
	}
	
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value,
	                                  boolean includeDeleted) {
		List<T> resultList = null;
		JPACriteriaQuery<T> jpaCriteriaQuery =
				JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache);
		if (value == null) {
			jpaCriteriaQuery.isNull(propertyName);
		} else {
			jpaCriteriaQuery.eq(propertyName, value);
		}
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		resultList = jpaCriteriaQuery.list();
		return resultList.isEmpty() ? null : resultList.get(0);
	}
	
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value,
	                                  OrderBean... orders) {
		return findUniqueByProperty(entityClass, propertyName, value, true, orders);
	}
	
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value,
	                                  boolean includeDeleted, OrderBean... orders) {
		List<T> resultList = findByProperty(entityClass, propertyName, value, includeDeleted, orders);
		return resultList.isEmpty() ? null : resultList.get(0);
	}
	
	@Override
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates) {
		return findUniqueByProperties(entityClass, predicates, true);
	}
	
	@Override
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted) {
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicates(predicates);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		List<T> results = jpaCriteriaQuery.list();
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates, OrderBean... orders) {
		return findUniqueByProperties(entityClass, predicates, true, orders);
	}
	
	@Override
	public <T> T findUniqueByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted,
	                                    OrderBean... orders) {
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicates(predicates)
				.addOrders(orders);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		List<T> results = jpaCriteriaQuery.list();
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public <T> Optional<T> findOne(Class<T> entityClass, String propertyName, Object value) {
		return findOne(entityClass, propertyName, value, true);
	}
	
	@Override
	public <T> Optional<T> findOne(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted) {
		List<T> resultList = findByProperty(entityClass, propertyName, value, includeDeleted);
		return resultList.isEmpty() ? Optional.of(null) : Optional.of(resultList.get(0));
	}
	
	@Override
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, OrderBean... orders) {
		return findByProperties(entityClass, predicates, true, orders);
	}
	
	@Override
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted,
	                                    OrderBean... orders) {
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicates(predicates)
				.addOrders(orders);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, Page page) {
		return findByProperties(entityClass, predicates, true, page);
	}
	
	@Override
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted,
	                                    Page page) {
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicates(predicates)
				.setPage(page);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates) {
		return findByProperties(entityClass, predicates, true);
	}
	
	@Override
	public <T> List<T> findByProperties(Class<T> entityClass, List<Predicate> predicates, boolean includeDeleted) {
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicates(predicates);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	/**
	 * 给定一个值列表，查找某个属性值在这个列表里面的结果集，SQL的IN语法
	 *
	 * @param entityClass
	 * @param propertyName
	 * @param values
	 * @param orders
	 * @param <T>
	 * @return List<T>
	 */
	@Override
	public <T> List<T> findIn(Class<T> entityClass, final String propertyName, Collection<?> values,
	                          OrderBean... orders) {
		return findIn(entityClass, propertyName, values, true, orders);
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, final String propertyName, Collection<?> values,
	                          boolean includeDeleted, OrderBean... orders) {
		Objects.requireNonNull(propertyName);
		if (isEmpty(values)) {
			return new ArrayList<>();
		}
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.in(propertyName, values)
				.addOrders(orders);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, final String propertyName, Collection<?> values) {
		return findIn(entityClass, propertyName, values, true);
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> values,
	                          boolean includeDeleted) {
		Objects.requireNonNull(propertyName);
		if (isEmpty(values)) {
			return new ArrayList<>();
		}
		
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.in(propertyName, values);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	public <T, E> List<T> findIn(Class<T> entityClass, String propertyName, E[] values) {
		return findIn(entityClass, propertyName, values, true);
	}
	
	@Override
	public <T, E> List<T> findIn(Class<T> entityClass, String propertyName, E[] values, boolean includeDeleted) {
		Objects.requireNonNull(propertyName);
		if (values == null || values.length == 0) {
			return new ArrayList<>();
		}
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.in(propertyName, Arrays.asList(values));
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> values, Page page) {
		return findIn(entityClass, propertyName, values, true, page);
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> values,
	                          boolean includeDeleted, Page page) {
		Objects.requireNonNull(propertyName);
		if (isEmpty(values)) {
			return new ArrayList<>();
		}
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.in(propertyName, values)
				.setPage(page);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin,
	                               LocalDateTime end, OrderBean... orders) {
		return findBetween(entityClass, propertyName, begin, end, true, orders);
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin,
	                               LocalDateTime end, boolean includeDeleted, OrderBean... orders) {
		Objects.requireNonNull(propertyName);
		Objects.requireNonNull(begin);
		Objects.requireNonNull(end);
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.between(propertyName, begin, end)
				.addOrders(orders);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin,
	                               LocalDateTime end) {
		return findBetween(entityClass, propertyName, begin, end, true);
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, Long begin, Long end) {
		Objects.requireNonNull(propertyName);
		Objects.requireNonNull(begin);
		Objects.requireNonNull(end);
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.between(propertyName, begin, end);
		return jpaCriteriaQuery.list();
	}
	
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, Long begin, Long end, Page page) {
		Objects.requireNonNull(propertyName);
		Objects.requireNonNull(begin);
		Objects.requireNonNull(end);
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.between(propertyName, begin, end).setPage(page);
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin,
	                               LocalDateTime end, boolean includeDeleted) {
		Objects.requireNonNull(propertyName);
		Objects.requireNonNull(begin);
		Objects.requireNonNull(end);
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.between(propertyName, begin, end);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin,
	                               LocalDateTime end, Page page) {
		return findBetween(entityClass, propertyName, begin, end, true, page);
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin,
	                               LocalDateTime end, boolean includeDeleted, Page page) {
		Objects.requireNonNull(propertyName);
		Objects.requireNonNull(begin);
		Objects.requireNonNull(end);
		JPACriteriaQuery<T> jpaCriteriaQuery = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.between(propertyName, begin, end)
				.setPage(page);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findIsNull(Class<T> entityClass, String propertyName) {
		Objects.requireNonNull(propertyName, "propertyName cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery =
				JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache);
		jpaCriteriaQuery.isNull(propertyName);
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> findIsNull(Class<T> entityClass, String propertyName, boolean includeDeleted) {
		Objects.requireNonNull(propertyName, "propertyName cannot be null!");
		JPACriteriaQuery<T> jpaCriteriaQuery =
				JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache);
		jpaCriteriaQuery.isNull(propertyName);
		if (!includeDeleted) {
			jpaCriteriaQuery.eq("deleted", false);
		}
		return jpaCriteriaQuery.list();
	}
	
	@Override
	public <T> List<T> leftJoinFetch(Class<T> entityClass, Predicate predicate, String... attributeNames) {
		Objects.requireNonNull(predicate, "predicate cannot be null!");
		return JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicate(predicate)
				.leftJoinFetch(attributeNames)
				.list();
	}
	
	@Override
	public <T> List<T> leftJoinFetch(Class<T> entityClass, List<Predicate> predicates, String... attributeNames) {
		Objects.requireNonNull(predicates, "predicates cannot be null!");
		return JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicates(predicates)
				.leftJoinFetch(attributeNames)
				.list();
	}
	
	@Override
	public <T> T leftJoinFetchSingleResult(Class<T> entityClass, Predicate predicate, String... attributeNames) {
		Objects.requireNonNull(predicate, "predicate cannot be null!");
		List<T> results = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicate(predicate)
				.leftJoinFetch(attributeNames)
				.list();
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public <T> T leftJoinFetchSingleResult(Class<T> entityClass, Predicate predicate, List<OrderBean> orders,
	                                       String... attributeNames) {
		List<T> results = JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicate(predicate)
				.addOrders(orders)
				.leftJoinFetch(attributeNames)
				.list();
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public <T> List<T> find(String jpql, Class<T> clazz) {
		try {
			return find(jpql, null, null, clazz);
		} catch (Throwable e) {
			log.error("msg", e);
			throw new JPQLException(e);
		}
	}
	
	@Override
	public <T> T findUnique(String jpql, Class<T> clazz) {
		List<T> results = find(jpql, clazz);
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public <T> T findUnique(String jpql, Map<String, Object> params, Class<T> resultClass) {
		List<T> results = find(jpql, params, resultClass);
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public <T> T findUnique(String jpql, String paramName, Object paramValue, Class<T> clazz) {
		List<T> results = find(jpql, paramName, paramValue, clazz);
		return results.isEmpty() ? null : results.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String jpql, Map<String, Object> params, Class<T> resultClass) {
		try {
			return createQuery(jpql, params, resultClass).getResultList();
		} catch (Throwable e) {
			log.error("msg", e);
			throw new JPQLException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String jpql, String paramName, Object paramValue, Class<T> clazz) {
		try {
			return createQuery(jpql, paramName, paramValue, clazz).getResultList();
		} catch (Throwable e) {
			log.error("msg", e);
			throw new JPQLException(e);
		}
	}
	
	@Override
	public <T> List<T> namedQuery(String queryName, Map<String, Object> params, Class<T> clazz, Page page) {
		TypedQuery<T> query = entityManager.createNamedQuery(queryName, clazz);
		setParameters(query, params);
		if (page != null) {
			query.setMaxResults(page.getMaxResults());
			query.setFirstResult(page.getFirstResult());
		}
		return query.getResultList();
	}
	
	@Override
	public <T> List<T> namedQuery(String queryName, Map<String, Object> params, Class<T> clazz) {
		return namedQuery(queryName, params, clazz, null);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public <T> List<T> namedQuery(String queryName, String paramName, Object paramValue, Class<T> clazz) {
		//如果参数值是List类型，那么该SQL语句认为是IN查询, 如果List的size则为0, 就不需要查询了, 直接返回空的ArrayList
		if (paramValue instanceof List) {
			if (((List) paramValue).size() == 0) {
				return new ArrayList<>();
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (isNotBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return namedQuery(queryName, params, clazz, null);
	}
	
	@Override
	public <T> List<T> namedQuery(String queryName, Class<T> clazz) {
		return namedQuery(queryName, null, null, clazz);
	}
	
	/**
	 * 返回单个对象，不存在则返回null
	 *
	 * @param queryName
	 * @param paramName
	 * @param paramValue
	 * @param clazz
	 * @return T
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T> T namedSqlQuerySingleResult(String queryName, String paramName, Object paramValue, Class<T> clazz) {
		//如果参数值是List类型，那么该SQL语句认为是IN查询，如果List的size则为0，就不需要查询了，直接返回空的ArrayList
		if (paramValue instanceof List) {
			if (((List) paramValue).size() == 0) {
				return null;
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (isNotBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		List<T> results = namedSqlQuery(queryName, params, clazz, null);
		if (results.isEmpty()) {
			return null;
		}
		
		return results.get(0);
	}
	
	/**
	 * 返回单个对象，不存在则返回null
	 *
	 * @param queryName
	 * @param params
	 * @param clazz
	 * @param <T>
	 * @return T
	 */
	@Override
	public <T> T namedSqlQuerySingleResult(String queryName, Map<String, Object> params, Class<T> clazz) {
		List<T> results = namedSqlQuery(queryName, params, clazz, null);
		if (results.isEmpty()) {
			return null;
		}
		
		return results.get(0);
	}
	
	@Override
	public <T> T namedSqlQuerySingleResult(String queryName, Class<T> clazz) {
		List<T> results = namedSqlQuery(queryName, clazz);
		if (results.isEmpty()) {
			return null;
		}
		
		return results.get(0);
	}
	
	@SuppressWarnings({"unchecked", "deprecation"})
	@Override
	public <T> List<T> namedSqlQuery(String queryName, Map<String, Object> params, Class<T> clazz, Page page) {
		org.hibernate.query.Query<T> query = entityManager.createNamedQuery(queryName)
				.unwrap(org.hibernate.query.Query.class);
		String rawQuery = query.getQueryString();
		StringBuilder queryString = new StringBuilder(rawQuery);
		
		// 排序
		if (page != null) {
			boolean primaryOrdered = false;
			//优先取order
			if (page.getOrder() != null) {
				primaryOrdered = true;
				queryString.append(" ORDER BY ")
						.append(page.getOrder().getOrderBy()).append(" ")
						.append(page.getOrder().getDirection());
			}
			if (!page.getOrders().isEmpty()) { //2,3候选排序
				if (!primaryOrdered) {//没有提供page.order
					queryString.append(" ORDER BY ");
				} else {
					queryString.append(", ");
				}
				for (OrderBean orderBean : page.getOrders()) {
					queryString.append(orderBean.getOrderBy()).append(" ").append(orderBean.getDirection())
							.append(", ");
				}
			}
			if (queryString.lastIndexOf(", ") == queryString.length() - 2) {
				queryString.delete(queryString.length() - 2, queryString.length());
			}
			
			//如果没有提供排序，但是设置了默认排序，则采用create_time desc
			if (useDefaultOrder && queryString.indexOf("ORDER BY") == -1) {
				page.setOrder(order);
				queryString.append(" ORDER BY ")
						.append(page.getOrder().getOrderBy())
						.append(" ")
						.append(page.getOrder().getDirection());
			}
			
		}
		
		//建立context， 并放入数据  
		VelocityContext context = new VelocityContext();
		context.put("StringUtils", StringUtils.class);
		if (isNotEmpty(params)) {
			for (String paramName : params.keySet()) {
				context.put(paramName, params.get(paramName));
			}
		}
		for (String contextName : classMap.keySet()) {
			context.put(contextName, classMap.get(contextName));
		}
		//解析后数据的输出目标，java.io.Writer的子类  
		StringWriter sql = new StringWriter();
		//进行解析  
		Velocity.evaluate(context, sql, queryName, queryString.toString());
		
		String parsedSQL = sql.toString();
		query = entityManager
				.createNativeQuery(parsedSQL)
				.unwrap(org.hibernate.query.Query.class);
		query.setResultTransformer(ResultTransformerFactory.getResultTransformer(HashUtils.sha256(parsedSQL), clazz, hibernateQueryMode,
				enumLookupProperties));
		
		if (isNotEmpty(params)) {
			/*
			 * 如果params里面某个key对应的value是null, 下面query.setProperties(params)会抛NullpointException, 所以这里要移除值为null的key
			 */
			Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				if (entry.getValue() == null) {
					iterator.remove();
				}
			}
			for (String key : params.keySet()) {
				Object value = params.get(key);
				processInOperate(params, key, value);
			}
			query.setProperties(params);
		}
		
		if (page != null && !page.isPagingIgnore()) {
			query.setMaxResults(page.getMaxResults());
			query.setFirstResult(page.getFirstResult());
		}
		
		List<T> resultList;
		try {
			resultList = query.getResultList();
		} catch (Throwable e) {
			String msg = format("\nFailed to get resultlist from query\n{0}\n Parameters\n{1}!",
					sql,
					JacksonUtils.toJson(params));
			log.error(msg, e);
			throw new SQLQueryException(msg, e);
		}
		
		//接下来是分页查询中的查询总记录数
		if (page != null && page.isAutoCount()) {
			context.put(IS_COUNT_QUERY, true);
			//查总记录数不需要排序, 提升性能
			int orderByIndex = parsedSQL.toLowerCase().lastIndexOf("order by");
			if (orderByIndex!= -1) {
				parsedSQL = parsedSQL.substring(0, orderByIndex);
			}
			// 查询总记录数
			String countSql = SqlUtils.generateCountSql(parsedSQL);
			log.info("Count SQL: {}", countSql);
			org.hibernate.query.Query<T> countQuery = entityManager.createNativeQuery(countSql)
					.unwrap(org.hibernate.query.Query.class);
			if (isNotEmpty(params)) {
				countQuery.setProperties(params);
			}
			try {
				Integer totalRecords = ((BigInteger) countQuery.getSingleResult()).intValue();
				page.setTotalCount(totalRecords);
			} catch (Throwable e) {
				String msg = format("Failed to get result count from query[{0}] with parameters[{1}]!", countSql,
						JacksonUtils.toJson(params));
				throw new SQLCountQueryException(msg, e);
			}
		}
		return resultList;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public <T> List<T> namedSqlQuery(String queryName, String paramName, Object paramValue, Class<T> clazz,
	                                 Page page) {
		//如果参数值是List类型，那么该SQL语句认为是IN查询，如果List的size则为0，就不需要查询了，直接返回空的ArrayList
		if (paramValue instanceof List) {
			if (((List) paramValue).size() == 0) {
				return new ArrayList<>();
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return namedSqlQuery(queryName, params, clazz, page);
	}
	
	@Override
	public <T> List<T> namedSqlQuery(String queryName, Class<T> clazz, Page page) {
		return namedSqlQuery(queryName, null, null, clazz, page);
	}
	
	@Override
	public <T> List<T> namedSqlQuery(String queryName, Map<String, Object> params, Class<T> clazz) {
		return namedSqlQuery(queryName, params, clazz, null);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public <T> List<T> namedSqlQuery(String queryName, String paramName, Object paramValue, Class<T> clazz) {
		//如果参数值是List类型，那么该SQL语句认为是IN查询，如果List的斯则为0，就不需要查询了，直接返回空的ArrayList
		if (paramValue instanceof List) {
			if (((List) paramValue).size() == 0) {
				return new ArrayList<>();
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return namedSqlQuery(queryName, params, clazz);
	}
	
	@Override
	public <T> List<T> namedSqlQuery(String queryName, Class<T> clazz) {
		return namedSqlQuery(queryName, null, clazz);
	}
	
	@Override
	public List<?> namedRawSqlQuery(String queryName) {
		return namedRawSqlQuery(queryName, null, null);
	}
	
	@Override
	public List<?> namedRawSqlQuery(String queryName, String propertyName, Object value) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(propertyName)) {
			params.put(propertyName, value);
		}
		return namedRawSqlQuery(queryName, params);
	}
	
	@Override
	public List<?> namedRawSqlQuery(String queryName, Map<String, Object> params) {
		org.hibernate.query.Query<?> query = entityManager
				.createNamedQuery(queryName)
				.unwrap(org.hibernate.query.Query.class);
		
		String queryString = query.getQueryString();
		//建立context， 并放入数据  
		VelocityContext context = new VelocityContext();
		if (isNotEmpty(params)) {
			for (String paramName : params.keySet()) {
				context.put(paramName, params.get(paramName));
			}
		}
		for (String contextName : classMap.keySet()) {
			context.put(contextName, classMap.get(contextName));
		}
		//解析后数据的输出目标，java.io.Writer的子类  
		StringWriter sql = new StringWriter();
		//进行解析  
		Velocity.evaluate(context, sql, queryName, queryString);
		queryString = sql.toString();
		
		query = entityManager
				.createNativeQuery(queryString)
				.unwrap(org.hibernate.query.Query.class);
		
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		
		if (isNotEmpty(params)) {
			query.setProperties(params);
		}
		try {
			return query.getResultList();
		} catch (Throwable e) {
			String msg = format("Execute raw SQL query[{0}] with parameter[{1}] failed!", queryString,
					JacksonUtils.toJson(params));
			throw new RawSQLQueryException(msg, e);
		}
	}
	
	/**
	 * 用于查询记录条数
	 *
	 * @param queryName
	 * @return int
	 */
	public int namedCountQuery(String queryName) {
		return namedCountQuery(queryName, null, null);
	}
	
	public int namedCountQuery(String queryName, String paramName, Object paramValue) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return namedCountQuery(queryName, params);
	}
	
	public int namedCountQuery(String queryName, Map<String, Object> params) {
		List<?> result = namedRawSqlQuery(queryName, params);
		if (result.isEmpty()) {
			return 0;
		}
		
		return PrimitiveUtils.toInt(result.get(0));
	}
	
	@Override
	public boolean ifExists(String queryName, Map<String, Object> params) {
		return namedCountQuery(queryName, params) > 0;
	}
	
	@Override
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Boolean> query = criteriaBuilder.createQuery(Boolean.class);
		query.from(entityClass);
		query.select(criteriaBuilder.literal(true));
		
		Subquery<T> subquery = query.subquery(entityClass);
		Root<T> subRootEntity = subquery.from(entityClass);
		subquery.select(subRootEntity);
		
		Path<?> attributePath = subRootEntity.get(propertyName);
		javax.persistence.criteria.Predicate predicate =
				criteriaBuilder.equal(attributePath, criteriaBuilder.literal(value));
		Path<?> deletedAttributePath = subRootEntity.get("deleted");
		javax.persistence.criteria.Predicate deletedPredicate = criteriaBuilder.equal(deletedAttributePath,
				criteriaBuilder.literal(includeDeleted));
		subquery.where(predicate, deletedPredicate);
		query.where(criteriaBuilder.exists(subquery));
		
		TypedQuery<Boolean> typedQuery = entityManager.createQuery(query);
		List<Boolean> results = typedQuery.getResultList();
		if (results.isEmpty()) {
			return false;
		}
		return results.get(0);
	}
	
	@Override
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Boolean> query = criteriaBuilder.createQuery(Boolean.class);
		query.from(entityClass);
		query.select(criteriaBuilder.literal(true));
		
		Subquery<T> subquery = query.subquery(entityClass);
		Root<T> subRootEntity = subquery.from(entityClass);
		subquery.select(subRootEntity);
		
		Path<?> attributePath = subRootEntity.get(propertyName);
		javax.persistence.criteria.Predicate predicate = criteriaBuilder.equal(attributePath,
				criteriaBuilder.literal(value));
		subquery.where(predicate);
		query.where(criteriaBuilder.exists(subquery));
		
		TypedQuery<Boolean> typedQuery = entityManager.createQuery(query);
		List<Boolean> results = typedQuery.getResultList();
		if (results.isEmpty()) {
			return false;
		}
		return results.get(0);
	}
	
	@Override
	public <T> T namedScalarQuery(String queryName, Map<String, Object> params, Class<T> type) {
		Object result = namedRawSqlQuerySingleResult(queryName, params);
		return ValueHandlerFactory.convert(result, type);
	}
	
	@Override
	public <T> T namedScalarQuery(String queryName, String paramName, Object paramValue, Class<T> type) {
		Map<String, Object> params = new HashMap<>();
		params.put(paramName, paramValue);
		Object result = namedRawSqlQuerySingleResult(queryName, params);
		return ValueHandlerFactory.convert(result, type);
	}
	
	@Override
	public <T> List<T> namedScalarListQuery(String queryName, String paramName, Object paramValue, Class<T> type) {
		Map<String, Object> params = new HashMap<>();
		params.put(paramName, paramValue);
		return namedScalarListQuery(queryName, params, type);
	}
	
	@Override
	public <T> List<T> namedScalarListQuery(String queryName, Map<String, Object> params, Class<T> type) {
		return namedRawSqlQuery(queryName, params).stream()
				.map(result -> ValueHandlerFactory.convert(result, type))
				.collect(toList());
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public List namedScalarListQuery(String queryName, Map<String, Object> params, Class... types) {
		return (List) namedRawSqlQuery(queryName, params).stream()
				.map((results) -> {
					//results对应的是一行数据，它是Object[]类型的
					Object[] objects = (Object[]) results;
					for (int i = 0; i < objects.length; i++) {
						Object object = objects[i];
						objects[i] = ValueHandlerFactory.convert(object, types[i]);
					}
					return objects;
				}).collect(toList());
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public List<?> namedScalarListQuery(String queryName, Class... types) {
		return (List) namedRawSqlQuery(queryName, null).stream()
				.map((results) -> {
					//results对应的是一行数据，它是Object[]类型的
					Object[] objects = (Object[]) results;
					for (int i = 0; i < objects.length; i++) {
						Object object = objects[i];
						objects[i] = ValueHandlerFactory.convert(object, types[i]);
					}
					return objects;
				}).collect(toList());
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public <T> List<T> namedScalarListQuery(String queryName, Class<T> type) {
		return (List) namedRawSqlQuery(queryName, null).stream()
				.map((result) -> ValueHandlerFactory.convert(result, type))
				.collect(toList());
	}
	
	@Override
	@SuppressWarnings({"unchecked", "deprecation"})
	public <T> List<T> sqlQuery(String sql, Map<String, Object> params, Class<T> clazz) {
		org.hibernate.query.Query<T> query = entityManager.createNativeQuery(sql)
				.unwrap(org.hibernate.query.Query.class);
		if (clazz != null) {
			query.setResultTransformer(
					ResultTransformerFactory.getResultTransformer(sql, clazz, hibernateQueryMode, enumLookupProperties));
		}
		if (isNotEmpty(params)) {
			query.setProperties(params);
		}
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		try {
			return query.getResultList();
		} catch (Throwable e) {
			String msg = format("Execute query[{0}] with parameter[{1}] failed!", sql, JacksonUtils.toJson(params));
			throw new SQLQueryException(msg, e);
		}
	}
	
	@Override
	@SuppressWarnings({"unchecked", "deprecation"})
	public <T> List<T> sqlQuery(String sql, String countSql, Map<String, Object> params, Class<T> clazz,
	                            Page page) {
		org.hibernate.query.Query<T> query = null;
		// 排序
		if (page != null && page.getOrder() != null) {
			String queryStrWithSortOrder = sql + " ORDER BY " + page.getOrder().getOrderBy() + " "
					+ page.getOrder().getDirection();
			query = entityManager.createNativeQuery(queryStrWithSortOrder).unwrap(org.hibernate.query.Query.class);
		} else {
			query = entityManager.createNativeQuery(sql).unwrap(org.hibernate.query.Query.class);
		}
		
		query.setResultTransformer(
				ResultTransformerFactory.getResultTransformer(sql, clazz, hibernateQueryMode, enumLookupProperties));
		
		if (params != null && !params.isEmpty()) {
			query.setProperties(params);
		}
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		
		//获取总记录条数
		if (page != null) {
			if (StringUtils.isNotBlank(countSql)) {
				org.hibernate.query.Query<T> countQuery = entityManager
						.createNativeQuery(countSql)
						.unwrap(org.hibernate.query.Query.class);
				if (params != null && !params.isEmpty()) {
					countQuery.setProperties(params);
				}
				Integer totalRecords = ((BigInteger) countQuery.getSingleResult()).intValue();
				page.setTotalCount(totalRecords);
			}
			
			// Get data of required page
			query.setMaxResults(page.getMaxResults());
			query.setFirstResult(page.getFirstResult());
		}
		try {
			return query.getResultList();
		} catch (Throwable e) {
			String msg = format("Execute query[{0}] with parameter[{1}], pagination[{2}] and result class[{3}] failed!",
					sql, JacksonUtils.toJson(params), JacksonUtils.toJson(page), clazz.getName());
			throw new SQLQueryException(msg, e);
		}
	}
	
	@Override
	public <T> List<T> sqlQuery(String sql) {
		return sqlQuery(sql, null, null);
	}
	
	@Override
	public <T> List<T> sqlQuery(String sql, Class<T> clazz) {
		return sqlQuery(sql, null, clazz);
	}
	
	@Override
	public <T> List<T> sqlQuery(String sql, String paramName, Object paramValue, Class<T> clazz) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return sqlQuery(sql, params, clazz);
	}
	
	@Override
	public <T> List<T> sqlQuery(String sql, String countSql, String paramName, Object paramValue, Class<T> clazz,
	                            Page page) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return sqlQuery(sql, countSql, params, clazz);
	}
	
	@Override
	public <T> T sqlScalarQuery(String sql, Class<T> type) {
		Object result = sqlQuerySingleResult(sql, Collections.emptyMap());
		return ValueHandlerFactory.convert(result, type);
	}
	
	@Override
	public <T> T sqlScalarQuery(String sql, Map<String, Object> params, Class<T> type) {
		Object result = sqlQuerySingleResult(sql, params);
		return ValueHandlerFactory.convert(result, type);
	}
	
	@Override
	public <T> T sqlScalarQuery(String sql, String paramName, Object paramValue, Class<T> type) {
		Map<String, Object> params = new HashMap<>();
		params.put(paramName, paramValue);
		Object result = sqlQuerySingleResult(sql, params);
		return ValueHandlerFactory.convert(result, type);
	}
	
	public int sqlCountQuery(String sql, Map<String, Object> params) {
		Query query = entityManager.createNativeQuery(sql);
		for (String paramName : params.keySet()) {
			query.setParameter(paramName, params.get(paramName));
		}
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		Object count = query.getSingleResult();
		if (count instanceof BigInteger) {
			return ((BigInteger) count).intValue();
		}
		return ((Integer) count).intValue();
	}
	
	@Override
	public <T> JPACriteriaQuery<T> createJPACriteriaQuery(Class<T> entityClass,
	                                                      List<Predicate> predicates, OrderBean... orders) {
		return JPACriteriaQuery.from(entityClass, entityManager, hibernateUseQueryCache)
				.addPredicates(predicates)
				.addOrders(orders);
	}
	
	@Override
	public <T> int deleteByProperty(Class<T> entityClass, String propertyName,
	                                Object propertyValue) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaDelete<T> delete = criteriaBuilder.createCriteriaDelete(entityClass);
		Root<T> root = delete.from(entityClass);
		delete.where(criteriaBuilder.equal(root.get(propertyName), propertyValue));
		return entityManager.createQuery(delete).executeUpdate();
	}
	
	@Override
	public <T> int deleteByProperties(Class<T> entityClass, List<Predicate> predicates) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaDelete<T> delete = criteriaBuilder.createCriteriaDelete(entityClass);
		Root<T> root = delete.from(entityClass);
		List<javax.persistence.criteria.Predicate> conditions = new ArrayList<javax.persistence.criteria.Predicate>();
		predicates.forEach((predicate) -> {
			conditions.add(predicate.toPredicate(criteriaBuilder, root));
		});
		delete.where(conditions.toArray(new javax.persistence.criteria.Predicate[0]));
		return this.entityManager.createQuery(delete).executeUpdate();
	}
	
	@Override
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Collection<?> values) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(entityClass);
		Root<T> root = criteriaDelete.from(entityClass);
		criteriaDelete.where(root.get(propertyName).in(values));
		
		return entityManager.createQuery(criteriaDelete).executeUpdate();
	}
	
	@Override
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Object[] values) {
		requireNonNull(values, "values cannot be null");
		return deleteIn(entityClass, propertyName, Arrays.asList(values));
	}
	
	@Override
	public <T, PK extends Serializable> T ensureEntityExists(Class<T> entityClass,
	                                                         PK id) throws EntityNotFoundException {
		T entity = get(entityClass, id);
		if (entity == null) {
			throw new EntityNotFoundException(format("Unable to find {0} with id {1}", entityClass.getSimpleName(), id));
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T, PK extends Serializable> List<T> ensureMultiEntityExists(Class<T> entityClass, PK... ids) {
		Session session = entityManager.unwrap(Session.class);
		MultiIdentifierLoadAccess<T> multiIdentifierLoadAccess = session.byMultipleIds(entityClass);
		List<T> entities = multiIdentifierLoadAccess.multiLoad(ids);
		if (entities.size() != ids.length) {
			throw new EntityNotFoundException(format("Unable to find {0} with id {1}", entityClass.getSimpleName(), ids));
		}
		for (T entity : entities) {
			if (entity == null) {
				throw new EntityNotFoundException(format("Unable to find {0} with id {1}", entityClass.getSimpleName(), ids));
			}
		}
		return entities;
	}
	
	@Override
	public <T, PK extends Serializable> List<T> ensureMultiEntityExists(Class<T> entityClass, List<PK> ids) {
		requireNonNull(ids, "ids cannot be null!");
		Session session = entityManager.unwrap(Session.class);
		MultiIdentifierLoadAccess<T> multiIdentifierLoadAccess = session.byMultipleIds(entityClass);
		List<T> entities = multiIdentifierLoadAccess.multiLoad(ids);
		if (entities.size() != ids.size()) {
			throw new EntityNotFoundException(format("Unable to find {0} with id {1}", entityClass.getSimpleName(), ids));
		}
		for (T entity : entities) {
			if (entity == null) {
				throw new EntityNotFoundException(format("Unable to find {0} with id {1}", entityClass.getSimpleName(), ids));
			}
		}
		return entities;
	}
	
	@Override
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName,
	                                Object value) throws EntityNotFoundException {
		return ensureEntityExists(entityClass, propertyName, value, true);
	}
	
	@Override
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value,
	                                boolean includeDeleted) throws EntityNotFoundException {
		List<T> resultList = findByProperty(entityClass, propertyName, value, includeDeleted);
		T entity = resultList.isEmpty() ? null : resultList.get(0);
		if (entity == null) {
			throw new EntityNotFoundException(format("Unable to find {0} by property {1} with value {2}",
					entityClass.getSimpleName(), propertyName, value));
		}
		return entity;
	}
	
	@Override
	public <T> Query createQuery(String jpql, Class<T> resultClass) {
		Objects.requireNonNull(jpql, "jpql cannot be null");
		TypedQuery<T> query = entityManager.createQuery(jpql, resultClass);
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		return query;
	}
	
	@Override
	public <T> Query createQuery(final String jpql, String paramName, Object paramValue, Class<T> resultClass) {
		Objects.requireNonNull(jpql, "jpql cannot be null");
		TypedQuery<T> query = entityManager.createQuery(jpql, resultClass);
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		if (isNotBlank(paramName)) {
			query.setParameter(paramName, paramValue);
		}
		return query;
	}
	
	@Override
	public <T> void detach(T entity) {
		requireNonNull(entity, "entity cannot be null!");
		entityManager.detach(entity);
	}
	
	@Override
	public <T> void detach(List<T> entities) {
		requireNonNull(entities, "entities cannot be null!");
		entities.forEach((entity) -> {
			requireNonNull(entity);
			entityManager.detach(entity);
		});
	}
	
	@Override
	public Object sqlQuerySingleResult(String sql, Map<String, Object> params) {
		List<?> results = sqlQuery(sql, params);
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public List<?> sqlQuery(String sql, Map<String, Object> params) {
		org.hibernate.query.Query<?> query = entityManager
				.createNativeQuery(sql)
				.unwrap(org.hibernate.query.Query.class);
		
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		
		if (isNotEmpty(params)) {
			query.setProperties(params);
		}
		try {
			return query.getResultList();
		} catch (Throwable e) {
			String msg = format("Execute raw SQL query[{0}] with parameter[{1}] failed!", sql,
					JacksonUtils.toJson(params));
			throw new RawSQLQueryException(msg, e);
		}
	}
	
	@Override
	public Object namedRawSqlQuerySingleResult(String queryName, Map<String, Object> params) {
		List<?> results = namedRawSqlQuery(queryName, params);
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public Object namedRawSqlQuerySingleResult(String queryName, String paramName, Object paramValue) {
		Map<String, Object> params = new HashMap<>();
		if (!isBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return namedRawSqlQuerySingleResult(queryName, params);
	}
	
	@Override
	public Object namedRawSqlQuerySingleResult(String queryName) {
		List<?> results = namedRawSqlQuery(queryName, null);
		return results.isEmpty() ? null : results.get(0);
	}
	
	@Override
	public <T> T namedRawSqlQuerySingleResult(String queryName, String paramName, Object paramValue, Class<T> type) {
		Map<String, Object> params = new HashMap<>();
		if (!isBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		List<?> results = namedRawSqlQuery(queryName, params);
		Object result = results.isEmpty() ? null : results.get(0);
		if (result == null) {
			if (type.isPrimitive()) {
				return Defaults.defaultValue(type);
			}
			return null;
		}
		
		return ValueHandlerFactory.determineAppropriateHandler(type).convert(result);
	}
	
	@Override
	public int executeUpdate(String queryName, String paramName, Object paramValue) {
		Map<String, Object> params = new HashMap<>();
		if (!isBlank(paramName)) {
			params.put(paramName, paramValue);
		}
		return executeUpdate(queryName, params);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int executeUpdate(String queryName, Map<String, Object> params) {
		org.hibernate.query.Query<Integer> query =
				entityManager.createNamedQuery(queryName).unwrap(org.hibernate.query.Query.class);
		String rawQuery = query.getQueryString();
		
		//建立context， 并放入数据  
		VelocityContext context = new VelocityContext();
		context.put("StringUtils", StringUtils.class);
		if (isNotEmpty(params)) {
			for (String paramName : params.keySet()) {
				context.put(paramName, params.get(paramName));
			}
		}
		for (String contextName : classMap.keySet()) {
			context.put(contextName, classMap.get(contextName));
		}
		//解析后数据的输出目标，java.io.Writer的子类  
		StringWriter sql = new StringWriter();
		//进行解析  
		Velocity.evaluate(context, sql, queryName, rawQuery);
		
		String parsedSQL = sql.toString();
		query = entityManager.createNativeQuery(parsedSQL)
				.unwrap(org.hibernate.query.Query.class);
		
		if (isNotEmpty(params)) {
			for (String key : params.keySet()) {
				Object value = params.get(key);
				processInOperate(params, key, value);
			}
			query.setProperties(params);
		}
		
		try {
			return query.executeUpdate();
		} catch (Throwable e) {
			String msg = format("\nFailed to get resultlist from query\n{0}\n Parameters\n{1}!",
					sql,
					JacksonUtils.toJson(params));
			log.error(msg, e);
			throw new SQLQueryException(msg, e);
		}
		
	}
	
	@Override
	public void execute(String sql) {
		org.hibernate.query.Query query = entityManager.createNativeQuery(sql)
				.unwrap(org.hibernate.query.Query.class);
		
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		try {
			query.executeUpdate();
		} catch (Throwable e) {
			String msg = format("Execute query[{0}] failed!", sql);
			throw new SQLQueryException(msg, e);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		for (String contextName : getContextClasses().keySet()) {
			String className = getContextClasses().get(contextName);
			Class<?> contextClass;
			try {
				contextClass = Class.forName(className);
				if (contextClass != null) {
					classMap.put(contextName, contextClass);
				}
			} catch (ClassNotFoundException e) {
				log.error("Class [{}] does not exists", className);
			}
		}
	}
	
	@Override
	public void flush() {
		entityManager.flush();
	}
	
	/**
	 * Bind named parameters.
	 *
	 * @param params
	 */
	private <T> Query createQuery(final String jpql, final Map<String, ?> params, Class<T> resultClass) {
		Objects.requireNonNull(jpql, "jpql cannot be null");
		TypedQuery<T> query = entityManager.createQuery(jpql, resultClass);
		if (hibernateUseQueryCache) {
			query.setHint(HINT_QUERY_CACHE, true);
		}
		return setParameters(query, params);
	}
	
	/**
	 * 如果value是List或者数组，当他们是空、长度为0，则需要特殊处理一下，将value改写为'',这样SQL IN 语句才不会出错
	 *
	 * @param params
	 * @param key
	 * @param value
	 */
	private void processInOperate(Map<String, Object> params, String key, Object value) {
		if (value == null) {
			return;
		}
		
		if (value instanceof List) {
			List<?> values = (List<?>) value;
			if (values.size() == 0) {
				params.put(key, "''");
			}
			return;
		}
		
		ArrayTypes arrayTypes = ARRAY_TYPE_MAP.get(value.getClass().getName());
		if (arrayTypes == null) {
			return;
		}
		
		switch (arrayTypes) {
			case LONG_WRAPPER:
				Long[] arr1 = (Long[]) value;
				if (arr1.length == 0) {
					params.put(key, "''");
				}
				break;
			case LONG:
				long[] arr2 = (long[]) value;
				if (arr2.length == 0) {
					params.put(key, "''");
				}
				break;
			case INTEGER:
				int[] arr3 = (int[]) value;
				if (arr3.length == 0) {
					params.put(key, "''");
				}
				break;
			case INTEGER_WRAPPER:
				Integer[] arr4 = (Integer[]) value;
				if (arr4.length == 0) {
					params.put(key, "''");
				}
				break;
			case STRING:
				String[] arr5 = (String[]) value;
				if (arr5.length == 0) {
					params.put(key, "''");
				}
				break;
			case DOUBLE:
				double[] arr6 = (double[]) value;
				if (arr6.length == 0) {
					params.put(key, "''");
				}
				break;
			case DOUBLE_WRAPPER:
				Double[] arr7 = (Double[]) value;
				if (arr7.length == 0) {
					params.put(key, "''");
				}
				break;
			case FLOAT:
				float[] arr8 = (float[]) value;
				if (arr8.length == 0) {
					params.put(key, "''");
				}
				break;
			case FLOAT_WRAPPER:
				Float[] arr9 = (Float[]) value;
				if (arr9.length == 0) {
					params.put(key, "''");
				}
				break;
			
			default:
				break;
		}
	}
	
	private boolean isEmpty(Collection entities) {
		return entities == null || entities.isEmpty();
	}
	
	private boolean isNotEmpty(Collection entities) {
		return entities != null && !entities.isEmpty();
	}
	
	private boolean isEmpty(Map map) {
		return map == null || map.isEmpty();
	}
	
	private boolean isNotEmpty(Map map) {
		return map != null && !map.isEmpty();
	}
	
	private Query setParameters(Query query, Map<String, ?> params) {
		if (isEmpty(params)) {
			return query;
		}
		
		for (String paramName : params.keySet()) {
			query.setParameter(paramName, params.get(paramName));
		}
		return query;
	}
	
	public Map<String, String> getContextClasses() {
		return contextClasses;
	}
	
	public void setContextClasses(Map<String, String> contextClasses) {
		this.contextClasses = contextClasses;
	}
	
	public OrderBean getOrder() {
		return order;
	}
	
	public void setOrder(OrderBean order) {
		this.order = order;
	}
	
	public boolean isUseDefaultOrder() {
		return useDefaultOrder;
	}
	
	public void setUseDefaultOrder(boolean useDefaultOrder) {
		this.useDefaultOrder = useDefaultOrder;
	}
	
	public Set<String> getEnumLookupProperties() {
		return enumLookupProperties;
	}
	
	public void setEnumLookupProperties(Set<String> enumLookupProperties) {
		this.enumLookupProperties = enumLookupProperties;
	}
	
	
}
