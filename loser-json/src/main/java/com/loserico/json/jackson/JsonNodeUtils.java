package com.loserico.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.loserico.common.lang.utils.EnumUtils;
import lombok.extern.slf4j.Slf4j;

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
	
	public static <T> T readEnum(JsonNode rootNode, String nodeName, Class<?> clazz) {
		String value = readStr(rootNode, nodeName);
		if (isBlank(value)) {
			return null;
		}
		
		return (T) EnumUtils.lookupEnum(clazz, value);
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
}
