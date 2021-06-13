package com.loserico.search.builder.query;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.support.SearchHitsSupport;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 Search Template 查询
 * <p>
 * Copyright: (C), 2021-06-11 11:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticTemplateQueryBuilder {
	
	private String[] indices;
	
	private String templateName;
	
	private Map<String, Object> params = new HashMap<>();
	
	private Class resultType;
	
	public ElasticTemplateQueryBuilder(String... indices) {
		this.indices = indices;
	}
	
	public ElasticTemplateQueryBuilder templateName(String templateName) {
		this.templateName = templateName;
		return this;
	}
	
	public ElasticTemplateQueryBuilder params(Map<String, Object> params) {
		this.params.putAll(params);
		return this;
	}
	
	public ElasticTemplateQueryBuilder param(String paramName, Object paramValue) {
		this.params.put(paramName, paramValue);
		return this;
	}
	
	public ElasticTemplateQueryBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	
	/**
	 * 执行查询
	 *
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList() {
		SearchHit[] hits = searchHits();
		
		if (hits.length == 0) {
			return Collections.emptyList();
		}
		
		return SearchHitsSupport.toList(hits, resultType);
	}
	
	
	/**
	 * 执行查询, 返回一条记录
	 *
	 * @param <T>
	 * @return T
	 */
	public <T> T queryForOne() {
		SearchHit[] hits = searchHits();
		
		if (hits.length == 0) {
			return null;
		}
		
		SearchHit hit = hits[0];
		String source = hit.getSourceAsString();
		
		//如果没有source, 就不用管是否要封装成POJO了
		if (source == null) {
			return null;
		}
		
		//如果要封装成POJO对象
		if (ReflectionUtils.isPojo(resultType)) {
			return (T) JacksonUtils.toObject(source, resultType);
		}
		
		return (T) source;
	}
	
	
	/**
	 * 作为查询的公共部分抽取出来
	 *
	 * @return
	 */
	private SearchHit[] searchHits() {
		SearchResponse response = new SearchTemplateRequestBuilder(ElasticUtils.client)
				.setScript(templateName)
				.setScriptType(ScriptType.STORED)
				.setScriptParams(params)
				.setRequest(new SearchRequest(indices))
				.get().getResponse();
		return response.getHits().getHits();
	}
}
