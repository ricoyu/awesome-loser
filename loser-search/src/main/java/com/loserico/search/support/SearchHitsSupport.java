package com.loserico.search.support;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.cache.ElasticCacheUtils;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * ES查询返回的SearchHit[]处理类
 * <p>
 * Copyright: (C), 2021-04-29 11:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class SearchHitsSupport {
	
	/**
	 * 将SearchHit[]转成List
	 *
	 * @param hits
	 * @param resultType
	 * @param <T>
	 * @return List<T>
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> List<T> toList(SearchHit[] hits, Class resultType) {
		if (resultType == null) {
			return (List<T>) Arrays.stream(hits)
					.map(SearchHit::getSourceAsString)
					.collect(Collectors.toList());
		}
		
		//pojo标注了@DocId
		Field id = ElasticCacheUtils.idField(resultType);
		boolean hasDocId = id != null;
		//@DocId标注的字段是String类型
		boolean isStringId = hasDocId && id.getType() == String.class;
		
		return Arrays.stream(hits)
				.filter(Objects::nonNull)
				.map((hit) -> {
					String source = hit.getSourceAsString();
					if (isBlank(source)) {
						return null;
					}
					
					T obj = (T) JacksonUtils.toObject(source, resultType);
					if (hasDocId) {
						if (isStringId) {
							ReflectionUtils.setField(id, obj, hit.getId());
						} else {
							ReflectionUtils.setField(id, obj, Transformers.convert(hit.getId(), id.getType()));
						}
					}
					
					return obj;
				}).collect(Collectors.toList());
	}
	
	/**
	 * 从SearchHit[]中取script_fields, 并转成Map
	 *
	 * @param hits
	 * @return Map<String, Object>
	 */
	@SuppressWarnings({"unchecked"})
	public static Map<String, List<Object>> toScriptFieldsMap(SearchHit[] hits) {
		if (hits.length == 0) {
			return Collections.emptyMap();
		}
		
		Map<String, List<Object>> scriptFieldsMap = new HashMap<>();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			Map<String, DocumentField> fields = hit.getFields();
			for (Map.Entry<String, DocumentField> entry : fields.entrySet()) {
				if (!scriptFieldsMap.containsKey(entry.getKey())) {
					scriptFieldsMap.put((String) entry.getKey(), entry.getValue().getValues());
				} else {
					List<Object> values = scriptFieldsMap.get(entry.getKey());
					values.addAll(entry.getValue().getValues());
				}
				
			}
		}
		
		return scriptFieldsMap;
	}
	
	/**
	 * 拿本次的sort
	 *
	 * @param hits
	 * @return Object[]
	 */
	public static Object[] sortValues(SearchHit[] hits) {
		if (hits.length == 0) {
			return new Object[0];
		}
		
		SearchHit lastHit = hits[hits.length - 1];
		//拿到本次的sort
		return lastHit.getSortValues();
	}
}
