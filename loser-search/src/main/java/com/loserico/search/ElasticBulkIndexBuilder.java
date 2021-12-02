package com.loserico.search;

import com.loserico.search.cache.ElasticCacheUtils;
import com.loserico.search.support.BulkResult;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.loserico.common.lang.utils.Assert.notNull;
import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.search.ElasticUtils.ONLY_TYPE;
import static com.loserico.search.ElasticUtils.CLIENT;

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
public class ElasticBulkIndexBuilder {
	
	private String index;
	
	private List<?> docs;
	
	/**
	 * 是否要立即刷新
	 */
	private Boolean refresh;
	
	public ElasticBulkIndexBuilder(String index) {
		this.index = index;
	}
	
	/**
	 * 要批量插入的文档
	 * @param docs
	 * @return ElasticBulkIndexBuilder
	 */
	public ElasticBulkIndexBuilder docs(String... docs) {
		notNull(docs, "docs cannot be null!");
		if (docs.length == 0) {
			throw new IllegalArgumentException("docs cannot be empty!");
		}
		this.docs = Arrays.asList(docs);
		return this;
	}
	
	/**
	 * 要批量插入的文档
	 * @param docs
	 * @return ElasticBulkIndexBuilder
	 */
	public ElasticBulkIndexBuilder docs(List docs) {
		notNull(docs, "docs cannot be null!");
		if (docs.isEmpty()) {
			throw new IllegalArgumentException("docs cannot be empty!");
		}
		this.docs = docs;
		return this;
	}
	
	/**
	 * 批量插入后是否强制刷新, 这样可以立即查询到新插入的文档
	 * @param refresh
	 * @return ElasticBulkIndexBuilder
	 */
	public ElasticBulkIndexBuilder refresh(Boolean refresh) {
		this.refresh = refresh;
		return this;
	}
	
	public BulkResult execute() {
		BulkRequestBuilder bulkRequestBuilder = CLIENT.prepareBulk(index, ONLY_TYPE);
		if (refresh != null && refresh.booleanValue()) {
			bulkRequestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
		}
		docs.stream()
				.filter(Objects::nonNull)
				.map((doc) -> {
					String id = ElasticCacheUtils.getIdValue(doc);
					return CLIENT.prepareIndex()
							.setSource(toJson(doc), XContentType.JSON)
							.setId(id);
				}).forEach(builder -> bulkRequestBuilder.add(builder));
		BulkResponse itemResponses = bulkRequestBuilder.get();
		BulkResult bulkResult = new BulkResult();
		
		for (Iterator<BulkItemResponse> iterator = itemResponses.iterator(); iterator.hasNext(); ) {
			BulkItemResponse itemResponse = iterator.next();
			if (itemResponse.isFailed()) {
				//记录失败数
				bulkResult.fail();
				//记录失败message
				bulkResult.addFailMessage(itemResponse.getFailureMessage());
			} else {
				bulkResult.success();
				bulkResult.addId(itemResponse.getId());
			}
		}
		
		return bulkResult;
	}
}
