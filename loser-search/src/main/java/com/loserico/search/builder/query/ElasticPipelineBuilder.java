package com.loserico.search.builder.query;

import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.networking.utils.HttpUtils;
import com.loserico.search.exception.CreatePipelineException;
import com.loserico.search.support.RestSupport;
import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * URI Query 接收的查询参数
 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-search.html#search-search-api-query-params
 * <p>
 * Copyright: (C), 2021-04-28 17:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticPipelineBuilder {
	private static final String USERNAME = "elastic.username";
	private static final String PASSWORD = "elastic.password";
	
	/**
	 * 默认读取classpath下elastic.properties文件
	 */
	private static PropertyReader propertyReader = new PropertyReader("elastic");
	
	private static String username = propertyReader.getString(USERNAME);
	private static String password = propertyReader.getString(PASSWORD);
	
	/**
	 * pipeline的名字
	 */
	private String pipelineName;
	
	/**
	 * pipeline内容所在的文件名
	 */
	private String pipelineFileName;
	
	/**
	 * 直接给出pipeline的内容
	 */
	private String pipelineContent;
	
	public ElasticPipelineBuilder(String pipelineName) {
		this.pipelineName = pipelineName;
	}
	
	public ElasticPipelineBuilder pipeFilename(String pipelineFileName) {
		this.pipelineFileName = pipelineFileName;
		return this;
	}
	
	public ElasticPipelineBuilder pipelineContent(String pipelineContent) {
		this.pipelineContent = pipelineContent;
		return this;
	}
	
	/**
	 * 创建pipeline, 如果创建失败则抛异常
	 *
	 * @param <T>
	 * @return List<T>
	 */
	@SuppressWarnings({"unchecked"})
	public void create() {
		String queryString = buildQueryString();
		log.info("Create Pipeline Url: {}", queryString);
		
		String content = pipelineContent;
		if (isNotBlank(pipelineFileName)) {
			content = IOUtils.readClassPathFileAsString(pipelineFileName);
		}
	
		String responseJson = null;
		if (isNotBlank(username) && isNotBlank(password)) {
			responseJson = HttpUtils.put(queryString).basicAuth(username, password).body(content).request();
		} else {
			responseJson = HttpUtils.put(queryString).body(content).request();
		}
		boolean error = JsonPathUtils.ifExists(responseJson, "error");
		if (error) {
			String rootCause = JsonPathUtils.readNode(responseJson, "$.error.root_cause[0].reason");
			throw new CreatePipelineException(rootCause);
		}
	}
	
	/**
	 * 查看pipeline
	 */
	@SuppressWarnings({"unchecked"})
	public String get() {
		String queryString = buildQueryString();
		log.info("get Pipeline Url: {}", queryString);
		
		String responseJson = null;
		if (isNotBlank(username) && isNotBlank(password)) {
			responseJson = HttpUtils.get(queryString).basicAuth(username, password).request();
		} else {
			responseJson = HttpUtils.get(queryString).request();
		}
		boolean error = JsonPathUtils.ifExists(responseJson, "error");
		if (error) {
			String rootCause = JsonPathUtils.readNode(responseJson, "$.error.root_cause[0].reason");
			throw new CreatePipelineException(rootCause);
		}
		
		return responseJson;
		
	}
	/**
	 * 删除pipeline
	 */
	@SuppressWarnings({"unchecked"})
	public void delete() {
		String queryString = buildQueryString();
		log.info("Create Pipeline Url: {}", queryString);
		
		String responseJson = null;
		if (isNotBlank(username) && isNotBlank(password)) {
			responseJson = HttpUtils.delete(queryString).basicAuth(username, password).request();
		} else {
			responseJson = HttpUtils.delete(queryString).request();
		}
		boolean error = JsonPathUtils.ifExists(responseJson, "error");
		if (error) {
			String rootCause = JsonPathUtils.readNode(responseJson, "$.error.root_cause[0].reason");
			throw new CreatePipelineException(rootCause);
		}
		
	}
	
	private String buildQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append(RestSupport.HOSTS.get(0))
				.append("/").append("_ingest/pipeline/")
				.append(pipelineName);
		return sb.toString();
	}
}
