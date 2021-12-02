package com.loserico.search.builder.admin;

import com.loserico.search.ElasticUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;

import static com.loserico.common.lang.utils.Assert.notNull;

/**
 * <p>
 * Copyright: (C), 2021-03-11 13:53
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ElasticReindexBuilder {
	
	private QueryBuilder filter;
	
	private String srcIndex;
	
	private String destIndex;
	
	/**
	 * 可以把Reindex拆分成几个子任务并发执行
	 * Reindex supports Sliced scroll to parallelize the reindexing process. 
	 * This parallelization can improve efficiency and provide a convenient way to break the request down into smaller parts.
	 */
	private int slices;
	
	/**
	 * 一次操作多少文档
	 */
	private int size;
	
	public ElasticReindexBuilder(String srcIndex, String destIndex) {
		this.srcIndex = srcIndex;
		this.destIndex = destIndex;
	}
	
	/**
	 * 过滤出一部分文档进行Reindex
	 * @param filter
	 * @return ElasticReindexBuilder
	 */
	public ElasticReindexBuilder filter(QueryBuilder filter) {
		this.filter = filter;
		return this;
	}
	
	/**
	 * Reindex的目标索引
	 * @param destIndex
	 * @return ElasticReindexBuilder
	 */
	public ElasticReindexBuilder dest(String destIndex) {
		this.destIndex = destIndex;
		return this;
	}
	
	/**
	 * 可以把Reindex拆分成几个子任务并发执行
	 * @param slices
	 * @return ElasticReindexBuilder
	 */
	public ElasticReindexBuilder slices(int slices) {
		this.slices = slices;
		return this;
	}
	
	/**
	 * 一次batch Reindex多少文档
	 * @param size
	 * @return ElasticReindexBuilder
	 */
	public ElasticReindexBuilder size(int size) {
		this.size = size;
		return this;
	}
	
	/**
	 * 构造 ReindexRequestBuilder 对象, 方便添加更多Reindex选项
	 * @return ReindexRequestBuilder
	 */
	public ReindexRequestBuilder build() {
		notNull(srcIndex, "srcIndex 不能为null");
		notNull(destIndex, "destIndex 不能为null");
		
		ReindexRequestBuilder builder = new ReindexRequestBuilder(ElasticUtils.CLIENT, ReindexAction.INSTANCE)
				.source(srcIndex)
				.destination(destIndex);
		if (filter != null) {
			builder.filter(filter);
		}
		if (size != 0) {
			builder.size(size);
		}
		if (slices != 0) {
			builder.setSlices(slices);
		}
		return builder;
	}
	
	/**
	 * 直接执行Reindex操作, 返回Reindex结果
	 * @return BulkByScrollResponse
	 */
	public BulkByScrollResponse get() {
		BulkByScrollResponse response = build().get();
		log.info("Reindex response:\n{}", response.toString());
		return response;
	}
}
