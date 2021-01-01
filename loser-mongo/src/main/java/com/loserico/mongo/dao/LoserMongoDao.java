package com.loserico.mongo.dao;

import com.loserico.mongo.exception.MongoDatabaseNotInitializedException;
import com.loserico.mongo.utils.MongoUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2020-12-14 16:09
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class LoserMongoDao {
	
	/**
	 * MongoDatabase实例
	 */
	private static volatile MongoDatabase db = null;
	
	/**
	 * 初始化MongoDatabase并且保证db只被设置一次且不可更改<p/>
	 * 只在系统初始化的时候调用该方法完成LoserMongoDao的初始化, 业务代码不要调用我!!!
	 *
	 * @param database
	 */
	public static void _initialize(MongoDatabase database) {
		if (db == null) {
			synchronized (LoserMongoDao.class) {
				if (db == null) {
					db = database;
				} else {
					log.warn("MongoDatabase 已经被初始化了, 请勿重复初始化!");
				}
			}
		} else {
			log.warn("MongoDatabase 已经被初始化了, 请勿重复初始化!");
		}
	}
	
	/**
	 * 根据filter查询, 返回第一个对象
	 *
	 * @param filter
	 * @param collectionName 要查询的集合名称
	 * @return Document
	 */
	public static Document getOne(String collectionName, Document filter) {
		ensureInitialized();
		Objects.requireNonNull(collectionName, "collectionName不能为null");
		
		MongoCollection<Document> collection = db.getCollection(collectionName);
		if (filter != null) {
			return collection.find(filter).first();
		}
		
		return collection.find(new Document()).first();
	}
	
	/**
	 * 根据filter查询, 返回第一个对象
	 *
	 * @param filter
	 * @param collectionName 要查询的集合名称
	 * @param clazz          返回结果要封装到的对象Class
	 * @param <T>
	 * @return T
	 */
	public static <T> T getOne(String collectionName, Document filter, Class<T> clazz) {
		ensureInitialized();
		Objects.requireNonNull(collectionName, "collectionName不能为null");
		Objects.requireNonNull(clazz, "clazz不能为null");
		
		Document document = db.getCollection(collectionName).find(filter).first();
		return MongoUtils.toObject(document, clazz);
	}
	
	/**
	 * 根据主键查找
	 *
	 * @param collectionName 集合名称
	 * @param id             主键值
	 * @param clazz          返回的Entity类型
	 * @param <T>
	 * @return T
	 */
	public static <T> T getById(String collectionName, String id, Class<T> clazz) {
		if (isBlank(id)) {
			log.warn("id 为null!");
			return null;
		}
		
		Objects.requireNonNull(collectionName, "collectionName 不能为null");
		Objects.requireNonNull(clazz, "clazz不能为null");
		
		Document filter = new Document();
		filter.put("_id", new ObjectId(id.trim()));
		Document document = db.getCollection(collectionName).find(filter).first();
		return MongoUtils.toObject(document, clazz);
	}
	
	/**
	 * 查询
	 *
	 * @param collectionName
	 * @param filter
	 * @param sort
	 * @return MongoCursor<Document>
	 */
	public static MongoCursor<Document> iterator(String collectionName, Document filter, Document sort) {
		Objects.requireNonNull(collectionName, "collectionName 不能为null");
		MongoCollection<Document> collection = db.getCollection(collectionName);
		
		/*
		 * 过滤条件不为null
		 */
		if (filter != null) {
			FindIterable<Document> findIterable = collection.find(filter);
			/*
			 * 排序不为null
			 */
			if (sort != null) {
				return findIterable.sort(sort).iterator();
			} else {
				return findIterable.iterator();
			}
		}
		
		/*
		 * 表示过滤条件为null, 但是排序不为null
		 */
		if (sort != null) {
			return collection.find(new Document()).sort(sort).iterator();
		}
		
		return collection.find(new Document()).iterator();
	}
	
	/**
	 * 查询并返回POJO List
	 *
	 * @param collectionName 集合名称
	 * @param filter         过滤条件
	 * @param clazz          返回集合元素类型
	 * @param <T>
	 * @return List<T>
	 */
	public static <T> List<T> queryForList(String collectionName, Document filter, Class<T> clazz) {
		return queryForList(collectionName, filter, null, clazz);
	}
	
	/**
	 * 查询并返回POJO List
	 *
	 * @param collectionName 集合名称
	 * @param filter         过滤条件
	 * @param sort           排序条件
	 * @param clazz          返回集合元素类型
	 * @param <T>
	 * @return List<T>
	 */
	public static <T> List<T> queryForList(String collectionName, Document filter, Document sort, Class<T> clazz) {
		MongoCursor<Document> iterator = iterator(collectionName, filter, sort);
		return MongoUtils.toList(iterator, clazz);
	}
	
	/**
	 * 查询所有并返回POJO List
	 *
	 * @param collectionName 集合名称
	 * @param clazz          返回集合元素类型
	 * @param <T>
	 * @return List<T>
	 */
	public static <T> List<T> queryForAll(String collectionName, Class<T> clazz) {
		MongoCursor<Document> iterator = iterator(collectionName, null, null);
		return MongoUtils.toList(iterator, clazz);
	}
	
	/**
	 * MongoDB的$in查询
	 * 类似: db.emp.find({"salary": {$in: [10000, 8000]}})
	 *
	 * @param collectionName
	 * @param field
	 * @param values
	 * @param clazz
	 * @param <T>
	 * @return List<T>
	 */
	public static <T> List<T> queryIn(String collectionName, String field, List<?> values, Class<T> clazz) {
		if ("_id".equals(field)) {
			values = values.stream().map((value) -> {
				return new ObjectId(((String)value).trim());
			}).collect(toList());
		}
		Document document = new Document("$in", values);
		Document filter = new Document(field, document);
		MongoCursor<Document> iterator = iterator(collectionName, filter, null);
		return MongoUtils.toList(iterator, clazz);
	}
	
	private static void ensureInitialized() {
		if (db == null) {
			log.error("请先调用setDb(MongoDatabase database)方法完成初始化");
			throw new MongoDatabaseNotInitializedException();
		}
	}
}
