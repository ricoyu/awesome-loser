package com.loserico.orm.dao;

import com.loserico.common.lang.vo.Page;

import java.util.List;
import java.util.Map;

/**
 * 定义在XML中命名SQL查询接口，返回resultset将绑定到给定类型的Bean中。
 * ResultSet中field_name到Bean中property的映射规则为 foo_bar --> fooBar username --> username
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 * @since 2017-01-27 17:04
 */
public interface SQLOperations {
	
	/**
	 * 返回单个对象，不存在则返回null
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param paramName
	 * @param paramValue
	 * @param clazz
	 * @return
	 */
	public <T> T query4One(String queryName, String paramName, Object paramValue, Class<T> clazz);
	
	/**
	 * 返回单个对象，不存在则返回null
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param params
	 * @param clazz
	 * @param <T>
	 * @return T
	 */
	public <T> T query4One(String queryName, Map<String, Object> params, Class<T> clazz);
	
	/**
	 * 返回单个对象，不存在则返回null
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param clazz
	 * @param <T>
	 * @return T
	 */
	public <T> T query4One(String queryName, Class<T> clazz);
	
	/**
	 * 没有分页和参数的命名SQL查询
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param clazz
	 * @return List<T>
	 */
	public <T> List<T> query4List(String queryName, Class<T> clazz);
	
	/**
	 * 支持分页的命名SQL查询, 不带参数, 同时会自动调用queryName_count来获取总记录数
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param clazz
	 * @param page
	 * @return List<T>
	 */
	public <T> List<T> query4Page(String queryName, Class<T> clazz, Page page);
	
	/**
	 * 不带分页的命名SQL查询, 一个参数。
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param paramName
	 * @param paramValue
	 * @param clazz
	 * @return List<T>
	 */
	public <T> List<T> query4List(String queryName, String paramName, Object paramValue, Class<T> clazz);
	
	/**
	 * 支持分页的命名SQL查询, 支持一个参数, 同时会自动调用queryName_count来获取总记录数
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param paramName
	 * @param paramValue
	 * @param clazz
	 * @param page
	 * @return List<T>
	 */
	public <T> List<T> query4Page(String queryName, String paramName, Object paramValue, Class<T> clazz, Page page);
	
	/**
	 * 不带分页的命名SQL查询
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param params
	 * @param clazz
	 * @return List<T>
	 */
	public <T> List<T> query4List(String queryName, Map<String, Object> params, Class<T> clazz);
	
	/**
	 * 支持分页的命名SQL查询，同时会自动调用queryName_count来获取总记录数 支持Velocity风格的SQL模版
	 * 这个API，如果SQL有IN语句，但是传入的List类型参数size为0的话，会抛异常，需要自己在SQL里面用条件判断
	 * 支持Velocity风格的SQL模版
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @param params
	 * @param clazz
	 * @param page
	 * @return List<T>
	 */
	public <T> List<T> query4Page(String queryName, Map<String, Object> params, Class<T> clazz, Page page);
	
	/**
	 * 跟namedSqlQuery的差别就是结果集不封装到Bean里面
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @return List<T>
	 */
	public <T> List<T> query4RawList(String queryName);
	
	/**
	 * 跟namedSqlQuery的差别就是结果集不封装到Bean里面
	 * List里面存的是Object[], 数组长度取决于你SELECT了几个字段
	 *
	 * @param queryName 可以是定义在xx.hbm.xml中的sql-query的名字, 也可以是完整的一个SQL语句, 最新的loser-orm对这块更新支持了
	 * @return List<T>
	 */
	public <T> List<T> query4RawList(String queryName, String propertyName, Object value);
	
	/**
	 * 跟namedSqlQuery的差别就是结果集不封装到Bean里面
	 * RawSqlQuery跟sqlQuery的差别是结果不封装进POJO, 
	 * 而是转成Integer, Long, Float, BigDecimal, String, Boolean, Short等相对基本类型或者是LocalDateTime, Date, LocalDate, LocalTime类型
	 * 如果select了多个字段，那么返回的List里面存的是Object[], 数组长度取决于你SELECT了几个字段
	 *
	 * @param queryName
	 * @return List<T>
	 */
	public <T> List<T> query4RawList(String queryName, Map<String, Object> params);
	
	/**
	 * 跟namedSqlQuery的差别就是结果集不封装到Bean里面<p/>
	 * RawSqlQuery跟sqlQuery的差别是结果不封装进POJO, 
	 * 而是转成Integer, Long, Float, BigDecimal, String, Boolean, Short等相对基本类型或者是LocalDateTime, Date, LocalDate, LocalTime类型
	 *
	 * @param queryName
	 * @return Object
	 */
	public Object query4One(String queryName, Map<String, Object> params);
	
	/**
	 * 跟namedSqlQuery的差别就是结果集不封装到Bean里面
	 *
	 * @param queryName
	 * @return Object
	 */
	public Object query4One(String queryName, String paramName, Object paramValue);
	
	/**
	 * 根据SQL查询返回单个结果，并自动转换成期望对象类型<p/>
	 * RawSqlQuery跟sqlQuery的差别是结果不封装进POJO, 
	 * 而是转成Integer, Long, Float, BigDecimal, String, Boolean, Short等相对基本类型或者是LocalDateTime, Date, LocalDate, LocalTime类型
	 *
	 * @param queryName
	 * @param paramName
	 * @param paramValue
	 * @param type 返回值类型
	 * @return T
	 */
	public <T> T query4PrimitiveOne(String queryName, String paramName, Object paramValue, Class<T> type);
	
	/**
	 * 据SQL查询返回单个结果, JDBC返回的是什么类型这里也返回什么类型
	 * RawSqlQuery跟sqlQuery的差别是结果不封装进POJO, 
	 * 而是转成Integer, Long, Float, BigDecimal, String, Boolean, Short等相对基本类型或者是LocalDateTime, Date, LocalDate, LocalTime类型
	 * 
	 * @param queryName
	 * @return Object
	 */
	public Object query4One(String queryName);
	
	/**
	 * 返回单个值的查询 比如type是BigDecimal.class, 那么这个查询返回的是BigDecimal
	 * RawSqlQuery跟sqlQuery的差别是结果不封装进POJO, 
	 * 而是转成Integer, Long, Float, BigDecimal, String, Boolean, Short等相对基本类型或者是LocalDateTime, Date, LocalDate, LocalTime类型
	 *
	 * @param queryName
	 * @param params
	 * @param type
	 * @return T
	 */
	public <T> T query4Primitive(String queryName, Map<String, Object> params, Class<T> type);
	
	/**
	 * 返回单个值的查询 比如type是BigDecimal.class, 那么这个查询返回的是BigDecimal
	 * 
	 * @param queryName
	 * @param paramName
	 * @param paramValue
	 * @param type
	 * @param <T>
	 * @return T
	 */
	public <T> T query4Primitive(String queryName, String paramName, Object paramValue, Class<T> type);
	
	/**
	 * 返回单个值的列表查询 比如type是BigDecimal.class, 那么这个查询返回的是BigDecimal
	 *
	 * @param queryName
	 * @param paramName
	 * @param paramValue
	 * @param type
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> query4PrimitiveList(String queryName, String paramName, Object paramValue, Class<T> type);
	
	/**
	 * 返回单个值的列表查询 比如type是BigDecimal.class，那么这个查询返回的是BigDecimal
	 *
	 * @param queryName
	 * @param params
	 * @param type
	 * @return List<T>
	 */
	public <T> List<T> query4PrimitiveList(String queryName, Map<String, Object> params, Class<T> type);
	
	/**
	 * 查询返回多行数据，每行数据又有types.length列，将每列的值转成types对应的数据类型并返回
	 *
	 * @param queryName
	 * @param params
	 * @param types
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List query4PrimitiveList(String queryName, Map<String, Object> params, Class... types);
	
	/**
	 * 查询返回多行数据，每行数据又有types.length列，将每列的值转成types对应的数据类型并返回
	 *
	 * @param queryName
	 * @param types
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List query4PrimitiveList(String queryName, Class... types);
	
	/**
	 * 查询返回多行数据，每将每列的值转成type对应的数据类型并返回
	 *
	 * @param queryName
	 * @param type
	 * @return List<T>
	 */
	public <T> List<T> query4PrimitiveList(String queryName, Class<T> type);
	
	
	/**
	 * SQL 标量查询
	 *
	 * @param sql
	 * @param type
	 * @return T
	 */
	public <T> T sqlScalarQuery(String sql, Class<T> type);
	
	/**
	 * SQL 标量查询
	 *
	 * @param sql
	 * @param type
	 * @return T
	 */
	public <T> T sqlScalarQuery(String sql, Map<String, Object> params, Class<T> type);
	
	/**
	 * SQL 标量查询
	 *
	 * @param sql
	 * @param type
	 * @return T
	 */
	public <T> T sqlScalarQuery(String sql, String paramName, Object paramValue, Class<T> type);
	
	/**
	 * 根据给定的查询条件判断是否有符合条件的记录存在
	 *
	 * @param queryName
	 * @param params
	 * @return boolean
	 */
	public boolean ifExists(String queryName, Map<String, Object> params);
	
	/**
	 * 执行更新
	 *
	 * @param queryName
	 * @param params
	 * @return int 更新影响的记录数
	 */
	public int executeUpdate(String queryName, Map<String, Object> params);
	
	/**
	 * 执行更新操作
	 *
	 * @param queryName
	 * @param paramName
	 * @param paramValue
	 * @return int 更新影响的记录数
	 */
	public int executeUpdate(String queryName, String paramName, Object paramValue);
	
	/**
	 * 执行一条SQL语句, 什么都不返回
	 * @param sql
	 * @return
	 */
	public void execute(String sql);
	
	/**
	 * 非命名的SQL查询，不带参数, 不封装到对象
	 *
	 * @param sql
	 * @return List<T>
	 */
	public <T> List<T> sqlQuery(String sql);
	
	/**
	 * 非命名的SQL查询，不带参数
	 *
	 * @param sql
	 * @param clazz
	 * @return List<T>
	 */
	public <T> List<T> sqlQuery(String sql, Class<T> clazz);
	
	/**
	 * 非命名的SQL查询, 带一个参数
	 *
	 * @param sql
	 * @param paramName
	 * @param paramValue
	 * @param clazz
	 * @return
	 */
	public <T> List<T> sqlQuery(String sql, String paramName, Object paramValue, Class<T> clazz);
	
	/**
	 * 非命名的SQL查询, 带一个参数和分页
	 * @param sql
	 * @param countSql
	 * @param paramName
	 * @param paramValue
	 * @param clazz
	 * @param page
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> sqlQuery(String sql, String countSql, String paramName, Object paramValue, Class<T> clazz,
								Page page);
	
	/**
	 * 非命名的SQL查询
	 *
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return List<T>
	 */
	public <T> List<T> sqlQuery(String sql, Map<String, Object> params, Class<T> clazz);
	
	/**
	 * 非命名SQL，带参数和分页
	 *
	 * @param sql
	 * @param countSql
	 * @param params
	 * @param clazz
	 * @param page
	 * @return List<T>
	 */
	public <T> List<T> sqlQuery(String sql, String countSql, Map<String, Object> params, Class<T> clazz, Page page);
	
	/**
	 * 执行COUNT语句
	 * @param sql
	 * @param params
	 * @return int
	 */
	public int sqlCountQuery(String sql, Map<String, Object> params);
	
	/**
	 * 直接在Java代码里面写SQL语句查询, 结果不封装到Bean
	 *
	 * @param sql
	 * @param params
	 * @return List<?>
	 */
	public <T> List<T> sqlQuery(String sql, Map<String, Object> params);
	
	/**
	 * 直接在Java代码里面写SQL语句查询，结果不封装到Bean,返回单个结果
	 *
	 * @param sql
	 * @param params
	 * @return Object
	 */
	public Object sqlQuerySingleResult(String sql, Map<String, Object> params);
}
