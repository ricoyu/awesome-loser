package com.loserico.tokenparser.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.common.lang.utils.PrimitiveUtils;
import com.loserico.tokenparser.ognl.OgnlCache;
import com.loserico.tokenparser.utils.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-09-16 11:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OgnlTokenHandler implements TokenHandler {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private Object root;
	
	public OgnlTokenHandler(Object root) {
		this.root = root;
	}
	
	@Override
	public String handleToken(String ognlExpression) {
		if (root == null) {
			return null;
		}
		
		/*
		 * 如果root对象是简单类型, 那么封装成一个map
		 * 此时的ognlExpression应该是一个简单的名字, 如"name"
		 */
		if (singleValueCandidate(root)) {
			Map<String, Object> params = new HashMap<>(1);
			params.put(ognlExpression, root);
			root = params;
		}
		
		Object value = OgnlCache.getValue(ognlExpression, root);
		if (value == null) {
			return null;
		}
		
		//是字符串
		if (value instanceof String) {
			return (String) value;
		}
		
		//原子类型
		String primitiveStr = Strings.toString(value);
		if (primitiveStr != null) {
			return primitiveStr;
		}
		
		return toJson(value);
	}
	
	/**
	 * 判断root是否单值对象
	 * 单值对象为: 原子类型, String, 数组, Collection集合类
	 *
	 * @param root
	 * @return boolean
	 */
	private boolean singleValueCandidate(Object root) {
		if (root instanceof String) {
			return true;
		}
		if (PrimitiveUtils.isPrimitive(root)) {
			return true;
		}
		if (root.getClass().isArray()) {
			return true;
		}
		if (root instanceof Collection) {
			return true;
		}
		
		return false;
	}
	
	private String toJson(Object value) {
		try {
			return MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
