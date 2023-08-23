package com.loserico.search.support;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.cache.ElasticCacheUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
		List<String> highResults = new ArrayList<>();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
			Map<String, DocumentField> storedFields = hit.getFields();
				Map<String, String> resultMap = new HashMap<>();
				for(String field: storedFields.keySet()) {
					resultMap.put(field, storedFields.get(field).getValue());
				}
			if (highlightFieldMap != null && !highlightFieldMap.isEmpty()) {
				for (String field : highlightFieldMap.keySet()) {
					HighlightField highlightField = highlightFieldMap.get(field);
					resultMap.put(field, Arrays.toString(highlightField.getFragments()));
					highResults.add(JacksonUtils.toJson(resultMap));
					return (List<T>) highResults;
				}
			}
			String source = hit.getSourceAsString();
		}
		if (resultType == null || resultType == String.class) {
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
					if (resultType != null && resultType == Map.class) {
						//如果文档禁用了_source, 这里会是null
						Map<String, Object> resultMap = hit.getSourceAsMap();
						if (resultMap == null) {
							resultMap = new HashMap<>();
						}
						resultMap.put("_id", hit.getId());
						return (T) resultMap;
					}
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
	 * 将SearchHit[]转成List
	 *
	 * @param hits
	 * @param resultType
	 * @param <T>
	 * @return List<T>
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> T toObject(SearchHit[] hits, Class resultType) {
		if (hits.length == 0) {
			return null;
		}
		
		if (resultType != null && resultType == Map.class) {
			Map<String, Object> resultMap = hits[0].getSourceAsMap();
			resultMap.put("_id", hits[0].getId());
			return (T) resultMap;
		}
		
		String source = hits[0].getSourceAsString();
		if (resultType == null) {
			return (T) source;
		}
		
		if (isBlank(source)) {
			return null;
		}
		
		//pojo标注了@DocId
		Field id = ElasticCacheUtils.idField(resultType);
		boolean hasDocId = id != null;
		//@DocId标注的字段是String类型
		boolean isStringId = hasDocId && id.getType() == String.class;
		
		T obj = (T) JacksonUtils.toObject(source, resultType);
		if (hasDocId) {
			if (isStringId) {
				ReflectionUtils.setField(id, obj, hits[0].getId());
			} else {
				ReflectionUtils.setField(id, obj, Transformers.convert(hits[0].getId(), id.getType()));
			}
		}
		
		return obj;
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
	
	/**
	 * 总共命中多少条记录
	 * @param searchResponse
	 * @return long
	 */
	public static long totalHits(SearchResponse searchResponse) {
		return searchResponse.getHits().getTotalHits().value;
	}
}
