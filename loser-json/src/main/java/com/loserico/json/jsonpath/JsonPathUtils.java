package com.loserico.json.jsonpath;

import com.google.common.collect.Sets;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.loserico.json.collections.ExpiringHashMap;
import com.loserico.json.generic.ParameterizedTypeImpl;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.json.jsonpath.context.DocumentContext;
import com.loserico.json.jsonpath.context.JsonContext;
import com.loserico.json.jsonpath.mapper.JacksonMappingProvider;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * https://github.com/json-path/JsonPath
 * <p>
 * Operator		Description
 * $			The root element to query. This starts all path expressions.
 * json串的根元素,不管json是数组还是对象形式
 *
 * @			The current node being processed by a filter predicate.
 * 代表当前正在处理的item
 * *			Wildcard. Available anywhere a name or numeric are required.
 * ..			Deep scan. Available anywhere a name is required.
 * .<name>		Dot-notated child
 * [start:end]	Array slice operator
 * [?(<expression>)]		Filter expression. Expression must evaluate to a boolean value.
 * 过滤器很有用
 * ['<name>' (, '<name>')]	Bracket-notated child or children
 * [<number> (, <number>)]	Array index or indexes
 * <p>
 * 示例：
 * $.store.book[0].title
 * 或者
 * $['store']['book'][0]['title']
 * @.error 当前节点有没有error子节点
 * <p>
 * Copyright: Copyright (c) 2018-03-16 14:20
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 */
@Slf4j
public final class JsonPathUtils {
	
	private static ExpiringHashMap<String, DocumentContext> expireingCache = new ExpiringHashMap<>();
	
	static {
		Configuration.setDefaults(new Configuration.Defaults() {
			
			//需要com.fasterxml.jackson.core:jackson-databind:2.4.5
			private final JsonProvider jsonProvider = new JacksonJsonProvider();
			private final MappingProvider mappingProvider = new JacksonMappingProvider(JacksonUtils.objectMapper());
			
			@Override
			public Set<Option> options() {
				return Sets.newHashSet(Option.SUPPRESS_EXCEPTIONS);
			}
			
			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}
			
			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}
		});
	}
	
	/**
	 * @param json
	 * @param path
	 * @return
	 */
	public static boolean ifExists(String json, String path) {
		if (isBlank(json)) {
			return false;
		}
		Object result = getDocumentContext(json).read(path);
		if (result == null) {
			return false;
		}
		if (result instanceof JSONArray) {
			return ((JSONArray) result).size() != 0;
		}
		
		return true;
	}
	
	/**
	 * 通过Jsonpath读取某个节点，可能返回的是单个对象，也可能是集合对象
	 *
	 * @param json
	 * @param path
	 * @return
	 */
	public static <T> T readNode(String json, String path) {
		if (isBlank(json)) {
			return null;
		}
		return getDocumentContext(json).read(path);
	}
	
	public static <T> T readNodeIfExists(String json, String path) {
		if (isBlank(json)) {
			return null;
		}
		if (ifExists(json, path)) {
			return getDocumentContext(json).read(path);
		}
		return null;
	}
	
	/**
	 * 通过Jsonpath读取某个节点，可能返回的是单个对象，也可能是集合对象
	 *
	 * @param json
	 * @param path
	 * @return
	 */
	public static <T> T readNode(String json, String path, Class<T> clazz) {
		if (isBlank(json)) {
			return null;
		}
		return getDocumentContext(json).read(path, clazz);
	}
	
	public static <T> T readNodeIfExists(String json, String path, Class<T> clazz) {
		if (isBlank(json)) {
			return null;
		}
		
		if (ifExists(json, path)) {
			return getDocumentContext(json).read(path, clazz);
		}
		return null;
	}
	
	/**
	 * 示例1：给定json数组
	 * <pre>{@code
	 * [
	 *   {
	 *     "username": "hawk",
	 *     "error": {
	 *       "code": 899001,
	 *       "message": "user exist"
	 *     }
	 *   },
	 *   {
	 *     "username": "ricoyucsd",
	 *     "error": {
	 *       "code": 899001,
	 *       "message": "user exist"
	 *     }
	 *   },
	 *   {
	 *     "username": "ricoyussss",
	 *     "nickname": "三少爷",
	 *     "birthday": "1982-11-09",
	 *     "gender": 1,
	 *     "avatar": "qiniu/image/j/8D57A18AD6926D6D879DFA36B4ED9CC4.jpg"
	 *   }
	 * ]
	 * }</pre>
	 * 分别读取包含/不包含 error属性的元素<p>
	 * <pre>{@code
	 * JsonPathUtils.readListNode(result, "[?(@.error)].username", String.class)
	 * JsonPathUtils.readListNode(result, "[?(!@.error)].username", String.class)
	 * }</pre>
	 * <p>
	 * 示例2：给定json对象
	 * <pre>{@code
	 * {
	 *   "count": 2,
	 *   "total": 714,
	 *   "start": 0,
	 *   "users": [
	 *     {
	 *       "mtime": "2018-03-16 18:12:41",
	 *       "gender": 0,
	 *       "username": "96289794xcuqzz",
	 *       "ctime": "2018-03-16 18:12:41"
	 *     },
	 *     {
	 *       "mtime": "2018-03-16 18:12:41",
	 *       "gender": 0,
	 *       "username": "96269528oydxvk",
	 *       "ctime": "2018-03-16 18:12:41"
	 *     }
	 *   ]
	 * }
	 * }</pre>
	 * 取所有的username：$.users[*].username
	 *
	 * @param json
	 * @param path
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> readListNode(String json, String path, Class<T> clazz) {
		if (isBlank(json)) {
			return null;
		}
		Type type = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
		return getDocumentContext(json).read(path, type);
	}
	
	@SuppressWarnings({"unchecked"})
	public static List<String> readListNode(String json, String path) {
		if (isBlank(json)) {
			return null;
		}
		List results = getDocumentContext(json).read(path);
		return (List<String>) results.stream().map(result -> JacksonUtils.toJson(result)).collect(toList());
	}
	
	/**
	 * 通过Jsonpath读取某个节点，返回单个对象，如果是集合则取第一个
	 *
	 * @param json
	 * @param path
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> T readNodeSingleValue(String json, String path) {
		if (isBlank(json)) {
			return null;
		}
		Object result = getDocumentContext(json).read(path);
		if (result instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) result;
			if (jsonArray.size() == 0) {
				return null;
			}
			return (T) jsonArray.get(0);
		}
		if (result instanceof List) {
			List list = (List) result;
			if (list.size() == 0) {
				return null;
			}
			return (T) list.get(0);
		}
		return (T) result;
	}
	
	private static DocumentContext getDocumentContext(String json) {
		DocumentContext documentContext = expireingCache.computeIfAbsent(json, (x) -> {
			Configuration configuration = Configuration.defaultConfiguration();
			try {
				Object obj = configuration.jsonProvider().parse(json);
				return new JsonContext(obj, configuration);
			} catch (InvalidJsonException e) {
				log.error("", e);
				log.info("JSON格式有问题: {}", json);
				throw e;
			}
		}, 1, TimeUnit.MINUTES);
		return documentContext;
	}
	
}
