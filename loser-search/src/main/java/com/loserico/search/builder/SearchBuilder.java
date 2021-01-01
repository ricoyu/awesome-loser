package com.loserico.search.builder;

import com.loserico.search.enums.Direction;
import com.loserico.search.exception.IndexSearchException;
import com.loserico.search.support.Sort;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

/**
 * <p>
 * Copyright: (C), 2020-12-30 9:18
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class SearchBuilder {
	
	private RestHighLevelClient client;
	
	/**
	 * 要搜索的index
	 */
	private String[] indices;
	
	private Integer from;
	
	private Integer size;
	
	private TermQueryBuilder termQueryBuilder;
	
	/**
	 * 排序规则
	 */
	private List<Sort> sorts = new ArrayList<>();
	
	public SearchBuilder(RestHighLevelClient client, String... indices) {
		this.client = client;
		this.indices = indices;
	}
	
	public SearchBuilder termQuery(String field, String value) {
		termQueryBuilder = QueryBuilders.termQuery(field, value);
		return this;
	}
	
	/**
	 * 分页属性from
	 *
	 * @param from
	 * @return SearchBuilder
	 */
	public SearchBuilder from(Integer from) {
		this.from = from;
		return this;
	}
	
	/**
	 * 分页属性size
	 *
	 * @param size
	 * @return SearchBuilder
	 */
	public SearchBuilder size(Integer size) {
		this.size = size;
		return this;
	}
	
	/**
	 * 添加排序规则
	 *
	 * @param field     排序字段
	 * @param direction 排序规则
	 * @return SearchBuilder
	 */
	public SearchBuilder sort(String field, Direction direction) {
		sorts.add(Sort.of(field, direction));
		return this;
	}
	
	public Object execute() {
		SearchRequest searchRequest = new SearchRequest(indices);
		SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		searchSourceBuilder.query(termQueryBuilder);
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.fetchSource(true);
		
		//SearchRequestBuilder builder = new SearchRequestBuilder(client, SearchAction.INSTANCE).setIndices(indices);
		//builder.setQuery(QueryBuilders.boolQuery()
		//	.must(termQueryBuilder));
		
		try {
			SearchResponse response = client.search(searchRequest, DEFAULT);
			SearchHits hits = response.getHits();
			return Stream.of(hits.getHits())
					.map(SearchHit::getSourceAsString)
					.collect(toList());
		} catch (IOException e) {
			log.error("", e);
			throw new IndexSearchException(e);
		}
	}
}
