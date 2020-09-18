package com.loserico.mongo.dao;

import com.loserico.common.lang.vo.Page;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import java.util.List;

/**
 * 通过Mongo Shell Script 操作, 支持变量占位符 #{}, 如{"name": "#{name}"}
 * <p>
 * Copyright: (C), 2020-09-14 16:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface ScriptOperations {
	
	/**
	 * 通过Mongo Shell Script 查询
	 *
	 * @param json        Mongo Shell 脚本
	 * @param entityClass 实体类class
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, Class<T> entityClass);
	
	/**
	 * 通过Mongo Shell Script 查询
	 *
	 * @param json        Mongo Shell 脚本
	 * @param entityClass 实体类class
	 * @param param       参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, Class<T> entityClass, Object param);
	
	/**
	 * 通过Mongo Shell Script 查询
	 *
	 * @param json        Mongo Shell 脚本
	 * @param page        分页参数
	 * @param entityClass 实体类class
	 * @param param       参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, Page page, Class<T> entityClass, Object param);
	
	/**
	 * 通过Mongo Shell Script 查询
	 *
	 * @param json
	 * @param sortJson
	 * @param entityClass
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, String sortJson, Class<T> entityClass);
	
	/**
	 * 通过Mongo Shell Script 查询
	 *
	 * @param json        Mongo Shell 脚本
	 * @param sortJson    Mongo Shell 排序脚本
	 * @param entityClass 实体类class
	 * @param param       参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList(String json, String sortJson, Class<T> entityClass, Object param);
	
	/**
	 * 通过Mongo Shell Script 聚合查询
	 *
	 * @param scripts     聚合构件
	 * @param entityClass 实体类, 标注了@Document注解的类, 返回的结果类型
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> aggregationQuery(Class<T> entityClass, String... scripts);
	
	/**
	 * 通过Mongo Shell Script 聚合查询
	 *
	 * @param collectionName 集合名称
	 * @param scripts        聚合构件
	 * @param resultClass    返回的结果类型
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> aggregationQuery(String collectionName, Class<T> resultClass, String... scripts);
	
	/**
	 * 通过Mongo Shell Script 聚合查询
	 *
	 * @param scripts     聚合构件
	 * @param param       参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param entityClass 实体类
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> aggregationQuery(Class<T> entityClass, Object param, List<String> scripts);
	
	/**
	 * 通过Mongo Shell Script 聚合查询
	 *
	 * @param collectionName 集合名称
	 * @param scripts        聚合构件
	 * @param param          参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param resultClass    实体类
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> aggregationQuery(String collectionName, Class<T> resultClass, Object param, List<String> scripts);
	
	/**
	 * 通过Mongo Shell Script 查询单个结果, 如果有多个结果, 返回第一个
	 *
	 * @param json        Mongo Shell 脚本
	 * @param entityClass 实体类class
	 * @param <T>
	 * @return List<T>
	 */
	public <T> T findOne(String json, Class<T> entityClass);
	
	/**
	 * 通过Mongo Shell Script 查询单个结果, 如果有多个结果, 返回第一个
	 *
	 * @param json        Mongo Shell 脚本
	 * @param entityClass 实体类class
	 * @param param       参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return List<T>
	 */
	public <T> T findOne(String json, Class<T> entityClass, Object param);
	
	/**
	 * 通过Mongo Shell Script 查询
	 *
	 * @param json
	 * @param sortJson
	 * @param entityClass
	 * @param <T>
	 * @return T
	 */
	public <T> T findOne(String json, String sortJson, Class<T> entityClass);
	
	/**
	 * 通过Mongo Shell Script 查询
	 *
	 * @param json        Mongo Shell 脚本
	 * @param sortJson    Mongo Shell 排序脚本
	 * @param entityClass 实体类class
	 * @param param       参数, 可以是基本值类型, 对象, 数组, 集合, Map等
	 * @param <T>
	 * @return T
	 */
	public <T> T findOne(String json, String sortJson, Class<T> entityClass, Object param);
	
	/**
	 * 更新一篇文档
	 * 如: { "_id" : 1005, "name" : "Joshi", "marks" : { "english" : 15, "maths" : 18 }, "result" : "fail" }<br/>
	 * db.student.updateOne({"name": "Joshi"}, {$set: {"marks.english": 20}})
	 *
	 * @param query          查询语句 如 {"name": "Joshi"}
	 * @param update         更新语句 如 {$set: {"marks.english": 20}}
	 * @param collectionName 集合名称
	 * @return UpdateResult
	 */
	public UpdateResult updateOne(String collectionName, String query, String update);
	
	/**
	 * 更新一篇文档, 支持变量占位符#{varName}
	 * 如: { "_id" : 1005, "name" : "Joshi", "marks" : { "english" : 15, "maths" : 18 }, "result" : "fail" }<br/>
	 * db.student.updateOne({"name": "Joshi"}, {$set: {"marks.english": 20}})
	 *
	 * @param query          查询语句 如 {"name": "Joshi"}
	 * @param update         更新语句 如 {$set: {"marks.english": 20}}
	 * @param param          用于替换#{xxx}占位符的参数
	 * @param collectionName 集合名称
	 * @return UpdateResult
	 */
	public UpdateResult updateOne(String collectionName, String query, String update, Object param);
	
	
	/**
	 * 更新多篇文档
	 * 如: { "_id" : 1005, "name" : "Joshi", "marks" : { "english" : 15, "maths" : 18 }, "result" : "fail" }<br/>
	 * db.student.updateOne({"name": "Joshi"}, {$set: {"marks.english": 20}})
	 *
	 * @param query          查询语句 如 {"name": "Joshi"}
	 * @param update         更新语句 如 {$set: {"marks.english": 20}}
	 * @param collectionName 集合名称
	 * @return UpdateResult
	 */
	public UpdateResult updateMany(String collectionName, String query, String update);
	
	/**
	 * 更新多篇文档, 支持变量占位符#{varName}
	 * 如: { "_id" : 1005, "name" : "Joshi", "marks" : { "english" : 15, "maths" : 18 }, "result" : "fail" }<br/>
	 * db.student.updateOne({"name": "Joshi"}, {$set: {"marks.english": 20}})
	 *
	 * @param query          查询语句 如 {"name": "Joshi"}
	 * @param update         更新语句 如 {$set: {"marks.english": 20}}
	 * @param param          用于替换#{xxx}占位符的参数
	 * @param collectionName 集合名称
	 * @return UpdateResult
	 */
	public UpdateResult updateMany(String collectionName, String query, String update, Object param);
	
	/**
	 * 替换文档
	 * <ol>如果replacement是一个json字符串, 那么:
	 * <li/>不要包含_id字段
	 * <li/>如果非要有一个_id, 必须值跟被替换文档一致, 不可以不一样, 也不可以为null
	 * </ol>
	 * 
	 * <ol>如果replacement是一个POJO对象, 那么:
	 * <li/>_id字段可以为null
	 * <li/>_id字段跟被替换的文档值相等
	 * </ol>
	 *
	 * @param query
	 * @param replacement
	 * @param collectionName
	 * @return T 替换后的文档
	 */
	public <T> T replaceOne(String collectionName, String query, T replacement);
	
	public DeleteResult delete(String collectionName, String query);
	
	public <T> DeleteResult delete(String query, Class<T> entityClass);
}
