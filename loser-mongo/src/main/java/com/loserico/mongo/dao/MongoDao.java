package com.loserico.mongo.dao;

import com.google.common.collect.Iterables;
import com.loserico.common.lang.exception.EntityNotFoundException;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.Page;
import com.loserico.mongo.support.AggregationQuery;
import com.loserico.mongo.support.ScriptQuery;
import com.loserico.mongo.utils.Orders;
import com.loserico.tokenparser.utils.ParserUtils;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.ExecutableRemoveOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.loserico.common.lang.utils.Assert.notNull;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * <p>
 * Copyright: (C), 2020-08-20 10:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MongoDao implements EntityOperations, CriteriaOperations, ScriptOperations {
	
	/**
	 * JavaScript 文件后缀
	 */
	private static final String JS_SUFFIX = ".js";
	
	/**
	 * 默认在CLASSPATH下的mongo-scripts包下
	 */
	private static final String MONGO_SCRIPTS_DEFAULT_LOCATION = "mongo-scripts";
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private Operations operations;
	
	@PostConstruct
	public void init() {
		MongoConverter mongoConverter = ReflectionUtils.getFieldValue("mongoConverter", mongoTemplate);
		this.operations = new Operations(mongoConverter.getMappingContext());
	}
	
	@Override
	public <T> T save(T entity) {
		notNull(entity, "entity can not be null");
		return mongoTemplate.save(entity);
	}
	
	@Override
	public <T> List<T> save(List<T> entities) {
		notNull(entities, "entities can not be null");
		if (entities.isEmpty()) {
			return emptyList();
		}
		
		//Collection<T> results = mongoTemplate.insert(entities, entities.get(0).getClass());
		List<T> results = new ArrayList<>(entities.size());
		for (T entity : entities) {
			T result = mongoTemplate.save(entity);
			results.add(result);
		}
		return results;
	}
	
	@Override
	public <T> List<T> save(Set<T> entities) {
		if (entities.isEmpty()) {
			return emptyList();
		}
		
		Class<?> clazz = Iterables.getFirst(entities, null).getClass();
		
		List<T> results = new ArrayList<>(entities.size());
		for (T entity : entities) {
			T result = mongoTemplate.save(entity);
			results.add(result);
		}
		return results;
	}
	
	@Override
	public <T> void delete(T entity) {
		notNull(entity, "entities 不能为null");
		mongoTemplate.remove(entity);
	}
	
	@Override
	public <T> int delete(List<T> entities) {
		notNull(entities, "entities cannot be null");
		if (entities.size() == 0) {
			return 0;
		}
		
		List<Object> ids = entities.stream()
				.map(entity -> operations.forEntity(entity).getId())
				.distinct()
				.collect(toList());
		Class<T> clazz = (Class<T>) entities.get(0).getClass();
		return deleteByPk(clazz, (Serializable) ids);
		
	}
	
	@Override
	public <T, PK extends Serializable> int deleteByPk(Class<T> entityClass, PK id) {
		DeleteResult deleteResult = mongoTemplate.remove(Query.query(where("id").is(id)), entityClass);
		return Long.valueOf(deleteResult.getDeletedCount()).intValue();
	}
	
	@Override
	public <T, PK extends Serializable> int deleteByPk(Class<T> entityClass, List<PK> ids) {
		notNull(entityClass, "entityClass cannot be null");
		notNull(ids, "ids cannot be null");
		if (ids.size() == 0) {
			return 0;
		}
		BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, entityClass)
				.remove(Query.query(where("_id").in(ids)));
		BulkWriteResult bulkWriteResult = bulkOperations.execute();
		return bulkWriteResult.getDeletedCount();
	}
	
	@Override
	public <T, PK extends Serializable> int deleteAll(Class<T> entityClass) {
		ExecutableRemoveOperation.ExecutableRemove<T> executableRemove = mongoTemplate.remove(entityClass);
		Long deletedCount = executableRemove.all().getDeletedCount();
		return deletedCount.intValue();
	}
	
	@Override
	public <T, PK extends Serializable> T get(Class<T> entityClass, PK id) {
		return (T) mongoTemplate.findById(id, entityClass);
	}
	
	@Override
	public <T, PK extends Serializable> List<T> getMulti(Class<T> entityClass, PK... ids) {
		return mongoTemplate.find(Query.query(where("_id").in(ids)), entityClass);
	}
	
	@Override
	public <T, PK extends Serializable> List<T> getMulti(Class<T> entityClass, List<PK> ids) {
		return mongoTemplate.find(Query.query(where("_id").in(ids)), entityClass);
	}
	
	@Override
	public <T, PK extends Serializable> T ensureEntityExists(Class<T> entityClass, PK id) {
		T entity = get(entityClass, id);
		if (entity == null) {
			throw new EntityNotFoundException(format("Unable to find {0} by property {1} with value {2}",
					entityClass.getSimpleName(), "_id", id));
		}
		return null;
	}
	
	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		return mongoTemplate.findAll(entityClass);
	}
	
	@Override
	public <T> List<T> findAll(Class<T> entityClass, Page page) {
		notNull(page, "page can not be null");
		Query query = new Query();
		Long count = mongoTemplate.count(query, entityClass);
		query.skip(page.getFirstResult());
		query.limit(page.getPageSize());
		Sort sort = Orders.toSort(page);
		if (sort != null) {
			query.with(sort);
		}
		page.setTotalCount(count.intValue());
		return mongoTemplate.find(query, entityClass);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
		return mongoTemplate.find(Query.query(Criteria.where(propertyName).is(value)), entityClass);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, OrderBean... orderBeans) {
		Query query = Query.query(where(propertyName).is(value));
		Sort sort = Orders.toSort(orderBeans);
		query.with(sort);
		return mongoTemplate.find(query, entityClass);
	}
	
	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value, Page page) {
		notNull(page, "page can not be null");
		
		Query query = Query.query(where(propertyName).is(value));
		Long count = mongoTemplate.count(query, entityClass);
		page.setTotalCount(count.intValue());
		
		query.skip(page.getFirstResult());
		query.limit(page.getPageSize());
		Sort sort = Orders.toSort(page);
		if (sort != null) {
			query.with(sort);
		}
		
		return mongoTemplate.find(query, entityClass);
	}
	
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		return null;
	}
	
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value, OrderBean... orders) {
		return null;
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end) {
		return null;
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, OrderBean... orders) {
		return null;
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, Page page) {
		return null;
	}
	
	@Override
	public <T> List<T> findBetween(Class<T> entityClass, String propertyName, LocalDateTime begin, LocalDateTime end, boolean includeDeleted) {
		return null;
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value) {
		return null;
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value, OrderBean... orders) {
		return null;
	}
	
	@Override
	public <T> List<T> findIn(Class<T> entityClass, String propertyName, Collection<?> value, Page page) {
		return null;
	}
	
	@Override
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value) {
		return false;
	}
	
	@Override
	public <T> boolean ifExists(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted) {
		return false;
	}
	
	@Override
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value) throws EntityNotFoundException {
		return null;
	}
	
	@Override
	public <T> T ensureEntityExists(Class<T> entityClass, String propertyName, Object value, boolean includeDeleted) throws EntityNotFoundException {
		return null;
	}
	
	@Override
	public <T> int deleteByProperty(Class<T> entityClass, String propertyName, Object propertyValue) {
		return 0;
	}
	
	@Override
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Collection<?> value) {
		return 0;
	}
	
	@Override
	public <T> int deleteIn(Class<T> entityClass, String propertyName, Object... values) {
		return 0;
	}
	
	@Override
	public <T> List<T> queryForList(String json, Class<T> entityClass) {
		return (List<T>) mongoTemplate.find(new ScriptQuery(json), entityClass);
	}
	
	@Override
	public <T> List<T> queryForList(String json, Class<T> entityClass, Object param) {
		String content = ParserUtils.parse(json, param);
		return (List<T>) mongoTemplate.find(new ScriptQuery(content), entityClass);
	}
	
	@Override
	public <T> List<T> queryForList(String json, Page page, Class<T> entityClass, Object param) {
		String content = ParserUtils.parse(json, param);
		ScriptQuery query = new ScriptQuery(content);
		//查询总记录数
		Long count = mongoTemplate.count(query, entityClass);
		page.setTotalCount(count.intValue());
		
		query.skip(page.getFirstResult());
		query.limit(page.getPageSize());
		Sort sort = Orders.toSort(page);
		if (sort != null) {
			query.with(sort);
		}
		
		return mongoTemplate.find(query, entityClass);
	}
	
	@Override
	public <T> List<T> queryForList(String json, String sortJson, Class<T> entityClass) {
		ScriptQuery query = new ScriptQuery(json);
		query.with(Orders.toSort(sortJson));
		return (List<T>) mongoTemplate.find(query, entityClass);
	}
	
	@Override
	public <T> List<T> queryForList(String json, String sortJson, Class<T> entityClass, Object param) {
		String content = ParserUtils.parse(json, param);
		ScriptQuery query = new ScriptQuery(content);
		query.with(Orders.toSort(sortJson));
		return (List<T>) mongoTemplate.find(query, entityClass);
	}
	
	@Override
	public <T> List<T> aggregationQuery(String json, Class<T> outputType) {
		TypedAggregation<T> aggregation = Aggregation.newAggregation(outputType, new AggregationQuery(json));
		AggregationResults<T> aggregationResults = mongoTemplate.aggregate(aggregation, outputType);
		return aggregationResults.getMappedResults();
	}
}

