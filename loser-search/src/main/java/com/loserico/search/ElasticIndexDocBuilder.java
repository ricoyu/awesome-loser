package com.loserico.search;

import com.loserico.search.cache.ElasticCacheUtils;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.common.xcontent.XContentType;

import static com.loserico.common.lang.utils.Assert.notNull;
import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.search.ElasticUtils.CLIENT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2021-09-10 16:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticIndexDocBuilder {
	/**
	 * 唯一的一个type是_doc
	 */
	public static final String ONLY_TYPE = "_doc";
	
	private String index;
	
	private String doc;
	
	private String id;
	
	private String pipeline;
	/**
	 * 是否要立即刷新
	 */
	private boolean refresh;
	
	public ElasticIndexDocBuilder(String index) {
		this.index = index;
	}
	
	/**
	 * 要批量插入的文档
	 *
	 * @param doc
	 * @return ElasticBulkIndexBuilder
	 */
	public ElasticIndexDocBuilder doc(Object doc) {
		notNull(doc, "docs cannot be null!");
		if (doc instanceof String) {
			this.doc = (String) doc;
			return this;
		}
		String id = ElasticCacheUtils.getIdValue(doc);
		toJson(doc);
		return this;
	}
	
	public ElasticIndexDocBuilder pipeline(String pipeline) {
		this.pipeline = pipeline;
		return this;
	}
	
	public ElasticIndexDocBuilder id(String id) {
		this.id = id;
		return this;
	}
	
	public ElasticIndexDocBuilder id(int id) {
		this.id = String.valueOf(id);
		return this;
	}
	
	
	/**
	 * 插入后是否强制刷新, 这样可以立即查询到新插入的文档
	 *
	 * @param refresh
	 * @return ElasticBulkIndexBuilder
	 */
	public ElasticIndexDocBuilder refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}
	
	public String execute() {
			IndexRequestBuilder indexRequestBuilder = CLIENT.prepareIndex(index, ONLY_TYPE, id)
					.setSource(doc, XContentType.JSON);
			
		if (isNotBlank(pipeline)) {
			indexRequestBuilder.setPipeline(pipeline);
		}
		if (refresh) {
			indexRequestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
		}
		IndexResponse response = indexRequestBuilder.get();
		return response.getId();
	}
}
