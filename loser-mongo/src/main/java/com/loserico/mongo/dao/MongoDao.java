package com.loserico.mongo.dao;

import com.google.common.collect.Iterables;
import com.loserico.common.lang.exception.EntityNotFoundException;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.Page;
import com.loserico.mongo.support.AggregationQuery;
import com.loserico.mongo.support.ExternalScriptsHelper;
import com.loserico.mongo.support.ScriptQuery;
import com.loserico.mongo.support.ScriptUpdate;
import com.loserico.mongo.utils.Orders;
import com.loserico.tokenparser.utils.ParserUtils;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
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
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.loserico.common.lang.utils.Assert.notNull;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
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
	
	@Autowired
	private ExternalScriptsHelper externalScriptsHelper;
	
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
		if (externalScriptsHelper.isFileName(json)) {
			String[] scripts = externalScriptsHelper.get(json);
			return doQueryForList(first(scripts), second(scripts), entityClass, null);
		}
		return doQueryForList(json, null, entityClass, null);
	}
	
	@Override
	public <T> List<T> queryForList(String json, Class<T> entityClass, Object param) {
		if (externalScriptsHelper.isFileName(json)) {
			String[] scripts = externalScriptsHelper.get(json);
			return doQueryForList(first(scripts), second(scripts), entityClass, param);
		}
		return doQueryForList(json, null, entityClass, param);
	}
	
	@Override
	public <T> List<T> queryForList(String json, Class<T> entityClass, Object param, Page page) {
		if (externalScriptsHelper.isFileName(json)) {
			json = externalScriptsHelper.getSingle(json);
		}
		ScriptQuery query = new ScriptQuery(json, param);
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
		if (externalScriptsHelper.isFileName(json)) {
			json = externalScriptsHelper.getSingle(json);
		}
		return doQueryForList(json, sortJson, entityClass, null);
	}
	
	@Override
	public <T> List<T> queryForList(String json, String sortJson, Class<T> entityClass, Object param) {
		if (externalScriptsHelper.isFileName(json)) {
			json = externalScriptsHelper.getSingle(json);
		}
		return doQueryForList(json, sortJson, entityClass, param);
	}
	
	private <T> List<T> doQueryForList(String json, String sortJson, Class<T> entityClass, Object param) {
		ScriptQuery query = new ScriptQuery(json, param);
		
		if (isNotBlank(sortJson)) {
			String sortContent = ParserUtils.parse(sortJson, param);
			query.with(Orders.toSort(sortContent));
		}
		
		return (List<T>) mongoTemplate.find(query, entityClass);
	}
	
	@Override
	public <T> List<T> aggregationQuery(Class<T> entityClass, String filename) {
		String[] scripts = externalScriptsHelper.get(filename);
		AggregationQuery[] aggregationQueries = Arrays.stream(scripts)
				.filter(Objects::nonNull)
				.map(script -> new AggregationQuery(script))
				.toArray(AggregationQuery[]::new);
		TypedAggregation<T> aggregation = Aggregation.newAggregation(entityClass, aggregationQueries);
		AggregationResults<T> aggregationResults = mongoTemplate.aggregate(aggregation, entityClass);
		return aggregationResults.getMappedResults();
	}
	
	@Override
	public <T> List<T> aggregationQuery(String collection, Class<T> entityClass, String filename) {
		String[] scripts = externalScriptsHelper.get(filename);
		AggregationQuery[] aggregationQueries = Arrays.stream(scripts)
				.filter(Objects::nonNull)
				.map(script -> new AggregationQuery(script))
				.toArray(AggregationQuery[]::new);
		TypedAggregation<T> aggregation = Aggregation.newAggregation(entityClass, aggregationQueries);
		AggregationResults<T> aggregationResults = mongoTemplate.aggregate(aggregation, collection, entityClass);
		return aggregationResults.getMappedResults();
	}
	
	@Override
	public <T> List<T> aggregationQuery(Class<T> entityClass, Object param, String filename) {
		String[] scripts = externalScriptsHelper.get(filename);
		AggregationQuery[] aggregationQueries = Arrays.stream(scripts)
				.map((script) -> {
					return new AggregationQuery(script, param);
				}).toArray(AggregationQuery[]::new);
		TypedAggregation<T> aggregation = Aggregation.newAggregation(entityClass, aggregationQueries);
		AggregationResults<T> aggregationResults = mongoTemplate.aggregate(aggregation, entityClass);
		return aggregationResults.getMappedResults();
	}
	
	@Override
	public <T> List<T> aggregationQuery(String collection, Class<T> entityClass, Object param, String filename) {
		String[] scripts = externalScriptsHelper.get(filename);
		AggregationQuery[] aggregationQueries = Arrays.stream(scripts)
				.map((script) -> {
					return new AggregationQuery(script, param);
				}).toArray(AggregationQuery[]::new);
		TypedAggregation<T> aggregation = Aggregation.newAggregation(entityClass, aggregationQueries);
		AggregationResults<T> aggregationResults = mongoTemplate.aggregate(aggregation, collection, entityClass);
		return aggregationResults.getMappedResults();
	}
	
	@Override
	public <T> T findOne(String json, Class<T> entityClass) {
		if (externalScriptsHelper.isFileName(json)) {
			String[] scripts = externalScriptsHelper.get(json);
			return doFindOne(first(scripts), second(scripts), entityClass, null);
		}
		return doFindOne(json, null, entityClass, null);
	}
	
	@Override
	public <T> T findOne(String json, Class<T> entityClass, Object param) {
		if (externalScriptsHelper.isFileName(json)) {
			String[] scripts = externalScriptsHelper.get(json);
			return doFindOne(first(scripts), second(scripts), entityClass, param);
		}
		return doFindOne(json, null, entityClass, param);
	}
	
	@Override
	public <T> T findOne(String json, String sortJson, Class<T> entityClass) {
		return doFindOne(json, sortJson, entityClass, null);
	}
	
	@Override
	public <T> T findOne(String json, String sortJson, Class<T> entityClass, Object param) {
		return doFindOne(json, sortJson, entityClass, param);
	}
	
	private <T> T doFindOne(String json, String sortJson, Class<T> entityClass, Object param) {
		ScriptQuery query = new ScriptQuery(json, param);
		
		if (isNotBlank(sortJson)) {
			String sortContent = ParserUtils.parse(sortJson, param);
			query.with(Orders.toSort(sortContent));
		}
		
		return mongoTemplate.findOne(query, entityClass);
	}
	
	@Override
	public UpdateResult updateOne(String collectionName, String filename) {
		String[] scripts = externalScriptsHelper.get(filename);
		return doUpdateOne(collectionName, first(scripts), second(scripts), null);
	}
	
	@Override
	public UpdateResult updateOne(String collectionName, String filename, Object param) {
		String[] scripts = externalScriptsHelper.get(filename);
		return doUpdateOne(collectionName, first(scripts), second(scripts), param);
	}
	
	@Override
	public UpdateResult updateOne(String collectionName, String query, String update) {
		return doUpdateOne(collectionName, query, update, null);
	}
	
	@Override
	public UpdateResult updateOne(String collectionName, String query, String update, Object param) {
		return doUpdateOne(collectionName, query, update, param);
	}
	
	private UpdateResult doUpdateOne(String collectionName, String query, String update, Object param) {
		notNull(query, "query cannot be null!");
		notNull(update, "update cannot be null!");
		ScriptQuery scriptQuery = new ScriptQuery(query, param);
		Update upd = ScriptUpdate.toUpdate(update, param);
		return mongoTemplate.updateFirst(scriptQuery, upd, collectionName);
	}
	
	@Override
	public UpdateResult updateMany(String collectionName, String filename) {
		String[] scripts = externalScriptsHelper.get(filename);
		return doUpdateMany(collectionName, first(scripts), second(scripts), null);
	}
	
	@Override
	public UpdateResult updateMany(String collectionName, String filename, Object param) {
		String[] scripts = externalScriptsHelper.get(filename);
		return doUpdateMany(collectionName, first(scripts), second(scripts), param);
	}
	
	@Override
	public UpdateResult updateMany(String collectionName, String query, String update) {
		return doUpdateMany(collectionName, query, update, null);
	}
	
	@Override
	public UpdateResult updateMany(String collectionName, String query, String update, Object param) {
		return doUpdateMany(collectionName, query, update, param);
	}
	
	private UpdateResult doUpdateMany(String collectionName, String query, String update, Object param) {
		notNull(query, "query cannot be null!");
		notNull(update, "update cannot be null!");
		ScriptQuery scriptQuery = new ScriptQuery(query, param);
		Update upd = ScriptUpdate.toUpdate(update, param);
		return mongoTemplate.updateMulti(scriptQuery, upd, collectionName);
	}
	
	@Override
	public <T> T replaceOne(String collectionName, String query, T replacement) {
		if (externalScriptsHelper.isFileName(query)) {
			query = externalScriptsHelper.getSingle(query);
		}
		return doReplaceOne(collectionName, query, replacement, null);
	}
	
	@Override
	public <T> T replaceOne(String collectionName, String query, T replacement, Object param) {
		if (externalScriptsHelper.isFileName(query)) {
			query = externalScriptsHelper.getSingle(query);
		}
		return doReplaceOne(collectionName, query, replacement, param);
	}
	
	private <T> T doReplaceOne(String collectionName, String query, T replacement, Object param) {
		ScriptQuery scriptQuery = new ScriptQuery(query, param);
		if (replacement instanceof String) {
			replacement = (T)ParserUtils.parse((String) replacement, param);
		}
		return mongoTemplate.findAndReplace(scriptQuery, replacement, collectionName);
	}
	
	@Override
	public DeleteResult delete(String collectionName, String query) {
		if (externalScriptsHelper.isFileName(query)) {
			query = externalScriptsHelper.getSingle(query);
		}
		ScriptQuery scriptQuery = new ScriptQuery(query);
		return mongoTemplate.remove(scriptQuery, collectionName);
	}
	
	@Override
	public <T> DeleteResult delete(String query, Class<T> entityClass) {
		if (externalScriptsHelper.isFileName(query)) {
			query = externalScriptsHelper.getSingle(query);
		}
		ScriptQuery scriptQuery = new ScriptQuery(query);
		return mongoTemplate.remove(scriptQuery, entityClass);
	}
	
	/**
	 * 取数组中第一个元素, 数组为null或者长度为0则返回null
	 *
	 * @param scripts
	 * @return String
	 */
	private String first(String[] scripts) {
		if (scripts != null && scripts.length > 0) {
			return scripts[0];
		}
		
		return null;
	}
	
	/**
	 * 取数组中第二个元素, 数组为null或者长度为0则返回null
	 *
	 * @param scripts
	 * @return
	 */
	private String second(String[] scripts) {
		if (scripts != null && scripts.length > 1) {
			return scripts[1];
		}
		return null;
	}
}

