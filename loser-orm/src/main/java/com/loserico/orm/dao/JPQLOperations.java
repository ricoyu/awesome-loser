package com.loserico.orm.dao;

import com.loserico.common.lang.vo.Page;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public interface JPQLOperations {

	/**
	 * 命名JPQL/HQL查询, 不带参数
	 * @param queryName
	 * @param clazz
	 * @return
	 */
	public <T> List<T> namedQuery(String queryName, Class<T> clazz);

	/**
	 * 命名JPQL/HQL查询
	 * 
	 * @param queryName
	 * @param paramName
	 * @param paramValue
	 * @param clazz
	 * @return
	 */
	public <T> List<T> namedQuery(String queryName, String paramName, Object paramValue, Class<T> clazz);

	/**
	 * 命名JPQL/HQL查询
	 * 
	 * @param queryName
	 * @param params
	 * @param clazz
	 * @return
	 */
	public <T> List<T> namedQuery(String queryName, Map<String, Object> params, Class<T> clazz);

	/**
	 * 支持分页的命名JPQL/HQL查询，同时会自动调用queryName_count来获取总记录数
	 * 
	 * @param queryName
	 * @param params
	 * @param clazz
	 * @param page
	 * @return
	 */
	public <T> List<T> namedQuery(String queryName, Map<String, Object> params, Class<T> clazz, Page page);

	public <T> List<T> find(String jpql, Class<T> clazz);
	
	public <T> T findUnique(String jpql, Class<T> clazz);
	
	public <T> T findUnique(String jpql, Map<String, Object> params, Class<T> clazz);
	
	public <T> T findUnique(String jpql, String paramName, Object paramValue, Class<T> clazz);

	/**
	 * 根据HQL或者JPQL查询
	 * 
	 * @param jpql
	 * @param params
	 * @return List<T>
	 */
	public <T> List<T> find(String jpql, Map<String, Object> params, Class<T> clazz);
	
	/**
	 * 根据HQL或者JPQL查询
	 * 
	 * @param jpql
	 * @param paramName
	 * @param paramValue
	 * @return List<T>
	 */
	public <T> List<T> find(String jpql, String paramName, Object paramValue, Class<T> clazz);
	
	/**
	 * 根据HQL或者JPQL查询,根据参数位置绑定
	 * 
	 * @param jpql
	 * @return
	 */
	public <T> Query createQuery(String jpql, Class<T> clazz);

	public <T> Query createQuery(String jpql, String paramName, Object paramValue, Class<T> clazz);
	
}
