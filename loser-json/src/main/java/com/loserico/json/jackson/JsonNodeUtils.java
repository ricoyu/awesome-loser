package com.loserico.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.loserico.common.lang.utils.EnumUtils;

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
public final class JsonNodeUtils {
	
	public static  String readStr(JsonNode rootNode, String nodeName) {
		JsonNode jsonNode = rootNode.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		String nodeValue = jsonNode.asText();
		return trim(nodeValue);
	}
	
	public static  <T> T readEnum(JsonNode rootNode, String nodeName, Class<?> clazz) {
		String value = readStr(rootNode, nodeName);
		if (isBlank(value)) {
			return null;
		}
		
		return (T) EnumUtils.lookupEnum(clazz, value);
	}
	
	public static  Long readLong(JsonNode node, String nodeName) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		return jsonNode.isNull() ? null : jsonNode.longValue();
	}
	
	public static  Integer readInt(JsonNode node, String nodeName) {
		JsonNode jsonNode = node.get(nodeName);
		if (jsonNode == null) {
			return null;
		}
		
		return jsonNode.isNull() ? null : jsonNode.intValue();
	}
}
