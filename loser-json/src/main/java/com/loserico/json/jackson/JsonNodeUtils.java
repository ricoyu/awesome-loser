package com.loserico.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.loserico.common.lang.utils.EnumUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.loserico.common.lang.utils.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2021-06-24 10:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class JsonNodeUtils {
	
	public static String readStr(JsonNode rootNode, String nodeName) {
		JsonNode jsonNode = rootNode.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		String nodeValue = jsonNode.asText();
		return trim(nodeValue);
	}
	
	/**
	 * 读取指定的字符串节点, 并指定默认值
	 *
	 * @param rootNode
	 * @param nodeName
	 * @param defaultValue 如果指定的Node不存在或者值为空, 那么使用默认值
	 * @return String
	 */
	public static String readStr(JsonNode rootNode, String nodeName, String defaultValue) {
		JsonNode jsonNode = rootNode.get(nodeName);
		if (jsonNode == null) {
			return defaultValue;
		}
		
		String nodeValue = jsonNode.asText();
		if (isBlank(nodeValue)) {
			return defaultValue;
		}
		return trim(nodeValue);
	}
	
	public static <T> T readEnum(JsonNode rootNode, String nodeName, Class<?> clazz) {
		String value = readStr(rootNode, nodeName);
		if (isBlank(value)) {
			return null;
		}
		
		return (T) EnumUtils.lookupEnum(clazz, value);
	}
	
	public static <T> T readEnum(JsonNode rootNode, String nodeName, Class<?> clazz, String property) {
		String value = readStr(rootNode, nodeName);
		if (isBlank(value)) {
			return null;
		}
		
		return (T) EnumUtils.lookupEnum(clazz, value, property);
	}
	
	public static Long readLong(JsonNode node, String nodeName) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null || jsonNode.isNull()) {
			return null;
		}
		if (jsonNode.getNodeType() == JsonNodeType.STRING) {
			String strValue = jsonNode.asText();
			try {
				return Long.parseLong(strValue);
			} catch (NumberFormatException e) {
				log.error("{} 节点读取到的值是 {}, 转成Long类型失败", nodeName, strValue);
			}
			return null;
		}
		return jsonNode.longValue();
	}
	
	public static Integer readInt(JsonNode node, String nodeName) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		return jsonNode.isNull() ? null : jsonNode.intValue();
	}
	
	/**
	 * 读取Boolean节点, 如果是字符串值: true/false 也能识别
	 *
	 * @param node
	 * @param nodeName
	 * @return Boolean
	 */
	public static Boolean readBoolean(JsonNode node, String nodeName) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		if (jsonNode.isNull()) {
			return null;
		}
		
		if (jsonNode.isBoolean()) {
			return jsonNode.booleanValue();
		}
		
		if (jsonNode.getNodeType() == JsonNodeType.STRING) {
			String strValue = jsonNode.asText();
			return Boolean.parseBoolean(strValue);
		}
		
		return null;
	}
	
	/**
	 * 读取字符串类型的数组节点
	 * <pre> {@code
	 * {
	 *   "dns_grouped_A": [
	 *     "180.101.49.11",
	 *     "180.101.49.12"
	 *   ]
	 * }
	 * }</pre>
	 *
	 * @param node
	 * @param nodeName
	 * @return String[]
	 */
	public static String[] readStrArr(JsonNode node, String nodeName) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		if (jsonNode.isNull()) {
			return null;
		}
		
		if (jsonNode.isArray()) {
			List<String> values = new ArrayList<>();
			Iterator<JsonNode> elements = jsonNode.elements();
			while (elements.hasNext()) {
				JsonNode childNode = elements.next();
				String nodeValue = childNode.textValue();
				values.add(nodeValue);
			}
			return values.stream().toArray(String[]::new);
		}
		
		return null;
	}
	
	/**
	 * 读取一个数组, 数组中的每一个元素又是一个对象类型
	 * <pre> {@code
	 * {
	 *   "dns_answers": [
	 *     {
	 *       "rrname": "www.baidu.com",
	 *       "rrtype": "CNAME",
	 *       "ttl": 600,
	 *       "rdata": "www.a.shifen.com"
	 *     },
	 *     {
	 *       "rrname": "www.a.shifen.com",
	 *       "rrtype": "A",
	 *       "ttl": 600,
	 *       "rdata": "180.101.49.11"
	 *     },
	 *     {
	 *       "rrname": "www.a.shifen.com",
	 *       "rrtype": "A",
	 *       "ttl": 600,
	 *       "rdata": "180.101.49.12"
	 *     }
	 *   ]
	 * }
	 * }</pre>
	 * @param node
	 * @param nodeName
	 * @param clazz
	 * @param <T>
	 * @return T[]
	 */
	public static <T> T[] readArr(JsonNode node, String nodeName, Class<T> clazz) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		if (jsonNode.isNull()) {
			return null;
		}
		
		if (jsonNode.isArray()) {
			List<T> values = new ArrayList<>();
			Iterator<JsonNode> elements = jsonNode.elements();
			while (elements.hasNext()) {
				JsonNode childNode = elements.next();
				String nodeValue = childNode.toString();
				T obj = JacksonUtils.toObject(nodeValue, clazz);
				values.add(obj);
			}
			return (T[]) values.stream().toArray();
		}
		
		return null;
	}
	
	/**
	 * 读取一个数组, 数组中的每一个元素又是一个对象类型
	 * <pre> {@code
	 * {
	 *   "dns_answers": [
	 *     {
	 *       "rrname": "www.baidu.com",
	 *       "rrtype": "CNAME",
	 *       "ttl": 600,
	 *       "rdata": "www.a.shifen.com"
	 *     },
	 *     {
	 *       "rrname": "www.a.shifen.com",
	 *       "rrtype": "A",
	 *       "ttl": 600,
	 *       "rdata": "180.101.49.11"
	 *     },
	 *     {
	 *       "rrname": "www.a.shifen.com",
	 *       "rrtype": "A",
	 *       "ttl": 600,
	 *       "rdata": "180.101.49.12"
	 *     }
	 *   ]
	 * }
	 * }</pre>
	 * @param node
	 * @param nodeName
	 * @param clazz
	 * @param <T>
	 * @return T[]
	 */
	public static <T> List<T> readList(JsonNode node, String nodeName, Class<T> clazz) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		if (jsonNode.isNull()) {
			return null;
		}
		
		if (jsonNode.isArray()) {
			List<T> values = new ArrayList<>();
			Iterator<JsonNode> elements = jsonNode.elements();
			while (elements.hasNext()) {
				JsonNode childNode = elements.next();
				String nodeValue = childNode.toString();
				T obj = JacksonUtils.toObject(nodeValue, clazz);
				values.add(obj);
			}
			return values;
		}
		
		return null;
	}
}
