package com.loserico.orm.utils;

import com.loserico.common.lang.utils.Arrays;
import com.loserico.common.lang.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 为SQLOperations构建查询参数的帮助类
 * <p>
 * Copyright: Copyright (c) 2017-12-07 10:52
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class QueryUtils {

	private static final Logger logger = LoggerFactory.getLogger(QueryUtils.class);

	/**
	 * 用于SQL查询中的 like子句的条件 <p> 
	 * 前缀匹配, 即返回xx%这样的格式, prefix两边的空格被去掉, 如果prefix是null或者空字符串则返回null
	 * 
	 * @param prefix
	 * @return String
	 */
	public static String prefixMatch(String prefix) {
		if (isNotBlank(prefix)) {
			return prefix.trim() + "%";
		}
		return null;
	}
	
	/**
	 * 用于SQL查询中的 like子句的条件 <p> 
	 * 前缀匹配, 即返回xx%这样的格式, prefix两边的空格被去掉, 如果prefix是null或者空字符串则返回null
	 * 
	 * @param paramValue
	 * @return String
	 */
	public static void prefixMatch(Map<String, Object> params, String paramName, String paramValue) {
		if (isNotBlank(paramValue)) {
			params.put(paramName, paramValue.trim() + "%");
		}
	}

	/**
	 * 用于SQL查询中的 like子句的条件 <p> 
	 * 后缀匹配, 即返回%xx这样的格式, prefix两边的空格被去掉, 如果prefix是null或者空字符串则返回null
	 * 
	 * @param prefix
	 * @return String
	 */
	public static String suffixMatch(String prefix) {
		if (isNotBlank(prefix)) {
			return "%" + prefix.trim();
		}
		return null;
	}
	
	/**
	 * 用于SQL查询中的 like子句的条件 <p> 
	 * 中间匹配, 即返回%xx%这样的格式, prefix两边的空格被去掉, 如果prefix是null或者空字符串则返回null
	 * 
	 * @param paramValue
	 * @return String
	 */
	public static void suffixMatch(Map<String, Object> params, String paramName, String paramValue) {
		if (isNotBlank(paramValue)) {
			params.put(paramName, "%" + paramValue.trim());
		}
	}

	/**
	 * 用于SQL查询中的 like子句的条件 <p>
	 * 中间匹配, 即返回%xx%这样的格式, prefix两边的空格被去掉, 如果prefix是null或者空字符串则返回null
	 * 
	 * @param prefix
	 * @return String
	 */
	public static String innerMatch(String prefix) {
		if (isNotBlank(prefix)) {
			return "%" + prefix.trim() + "%";
		}
		return null;
	}
	
	/**
	 * 用于SQL查询中的 like子句的条件 <p>
	 * 中间匹配, 即返回%xx%这样的格式, prefix两边的空格被去掉, 如果prefix是null或者空字符串则返回null
	 * 
	 * @param params
	 * @param paramName
	 * @param paramValue
	 */
	public static void innerMatch(Map<String, Object> params, String paramName, String paramValue) {
		if (isNotBlank(paramValue)) {
			params.put(paramName, "%" + paramValue.trim() + "%");
		}
	}

	/**
	 * 取enum的code属性作为查询条件
	 * 
	 * @param obj
	 * @return Integer
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Enum> Integer enumCode(T obj) {
		if (obj == null) {
			return null;
		}
		return ReflectionUtils.getFieldValue("code", obj);
	}

	/**
	 * 取enum的ordinal作为查询条件
	 * 
	 * @param obj
	 * @return Integer
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Enum> Integer enumOrdinal(T obj) {
		if (obj == null) {
			return null;
		}
		return obj.ordinal();
	}

	/**
	 * 取enum的name作为查询条件
	 * 
	 * @param obj
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Enum> String enumName(T obj) {
		if (obj == null) {
			return null;
		}
		return obj.name();
	}

	/**
	 * 如果enumObj有code属性并且值为-1, 表示查询所有, 则不会将这个查询条件加入params
	 * 
	 * @param params
	 * @param paramName		enum的某个属性
	 * @param queryValue	enum类型变量
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Enum> void enumCodeCondition(Map<String, Object> params, String paramName, T queryValue) {
		if (queryValue == null) {
			return;
		}

		Class clazz = queryValue.getDeclaringClass();
		Object code = null;
		try {
			code = ReflectionUtils.getFieldValue(queryValue, clazz, "code");
		} catch (IllegalArgumentException e) {
			logger.warn("{} 没有定义 code 属性, 忽略次查询条件", clazz.getSimpleName());
		}
		if (code == null) {
			return;
		}
		params.put(paramName, code);
	}
	
	/**
	 * 如果enumObj有code属性并且值为-1, 表示查询所有, 则不会将这个查询条件加入params
	 * 
	 * @param params
	 * @param queryValue
	 */
	@SuppressWarnings("rawtypes")
	public static void enumDescCondition(Map<String, Object> params, String paramName, Enum queryValue) {
		if (queryValue == null) {
			return;
		}
		
		Class clazz = queryValue.getDeclaringClass();
		Object code = null;
		try {
			code = ReflectionUtils.getFieldValue(queryValue, clazz, "desc");
		} catch (IllegalArgumentException e) {
			logger.warn("{} 没有定义 code 属性, 忽略次查询条件", clazz.getSimpleName());
		}
		if (code == null) {
			return;
		}
		params.put(paramName, code);
	}
	
	/**
	 * 如果enumObj有code属性并且值为-1, 表示查询所有, 则不会将这个查询条件加入params
	 * 
	 * @param params
	 * @param queryValue
	 */
	@SuppressWarnings("rawtypes")
	public static void enumOrdinalCondition(Map<String, Object> params, String paramName, Enum queryValue) {
		if (queryValue == null) {
			return;
		}
		params.put(paramName, queryValue.ordinal());
	}
	
	/**
	 * 如果enumObj有code属性并且值为-1, 表示查询所有, 则不会将这个查询条件加入params
	 * 
	 * @param params
	 * @param queryValue
	 */
	@SuppressWarnings("rawtypes")
	public static void enumNameCondition(Map<String, Object> params, String paramName, Enum queryValue) {
		if (queryValue == null) {
			return;
		}
		params.put(paramName, queryValue.name());
	}
	
	/**
	 * 用enum的属性prop作为查询条件paramName的值
	 * @param params
	 * @param paramName		SQL查询的变量名
	 * @param queryValue	enum实例
	 * @param prop			enum的某个属性名
	 */
	public static void enumPropCondition(Map<String, Object> params, String paramName, Enum queryValue, String prop) {
		if (queryValue == null || isBlank(prop)) {
			return;
		}
		Class clazz = queryValue.getDeclaringClass();
		Object propValue = null;
		try {
			propValue = ReflectionUtils.getFieldValue(queryValue, clazz, prop);
		} catch (IllegalArgumentException e) {
			logger.warn("{} 没有定义 code 属性, 忽略次查询条件", clazz.getSimpleName());
		}
		if (propValue == null) {
			return;
		}
		params.put(paramName, propValue);
	}
	
	/**
	 * 设置数组类型的参数, 如果paramValues是null或者长度为0, 则不设置该参数。并且如果数组中某元素是null, 会过滤掉该元素
	 * @param params
	 * @param paramName
	 * @param paramValues
	 */
	public static void param(Map<String, Object> params, String paramName, Long[] paramValues) {
		if (paramValues != null && paramValues.length > 0) {
			params.put(paramName, Arrays.nonNull(paramValues));
		}
	}
	
	public static void param(Map<String, Object> params, String paramName, List<Long> paramValues) {
		if (paramValues != null && paramValues.size() > 0) {
			params.put(paramName, paramValues);
		}
	}
	
	public static void param(Map<String, Object> params, String paramName, Object paramValue) {
		if (paramValue != null) {
			params.put(paramName, paramValue);
		}
	}
}
