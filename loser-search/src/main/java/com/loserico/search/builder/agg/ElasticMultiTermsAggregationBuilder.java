package com.loserico.search.builder.agg;

import com.loserico.common.lang.vo.Page;
import com.loserico.search.builder.agg.sub.ElasticBucketSortSubAggregation;
import com.loserico.search.builder.agg.sub.SubAggregation;
import com.loserico.search.builder.agg.sub.SubAggregationSupport;
import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.AggResultSupport;
import com.loserico.search.support.SortSupport;
import com.loserico.search.vo.ElasticPage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Multi Terms 聚合, 这是 Bucket Aggregation <br/>
 * 基于多个字段的组合来计算分桶
 * <p>
 * Copyright: (C), 2021-05-10 11:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticMultiTermsAggregationBuilder extends AbstractAggregationBuilder implements TermAggregationBuilder, SubAggregatable, Compositable {
	
	/**
	 * 这个是限制返回桶的数量, 如果总共有10个桶, 但是size设为5, 那么聚合结果中只会返回前5个桶
	 */
	private Integer size;
	
	/**
	 * 帮助解决Terms不准的问题<br/>
	 * Terms 聚合分析不准的原因, 数据分散在多个不同的分片上, Coordinating Node 无法获取数据全貌<br/>
	 * 解决方案 1: 当数据量不大时, 设置Primary Shard为1; 实现准确性<br/>
	 * 方案 2: 在分布式数据上, 设置shard_size参数, 提高精确度<br/>
	 * 原理: 每次从Shard上额外多获取数据, 提升准确率
	 */
	private Integer shardSize;
	
	/**
	 * 要对哪些字段聚合
	 */
	private String[] fields;
	
	/**
	 * 聚合分页的时候计算分页信息
	 */
	private Page page;
	
	/**
	 * 要对聚合的KEY或者COUNT排序
	 */
	protected List<SortOrder> sortOrders = new ArrayList<>();
	
	private ElasticMultiTermsAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticMultiTermsAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticMultiTermsAggregationBuilder(indices);
	}
	
	@Override
	public ElasticMultiTermsAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	public ElasticMultiTermsAggregationBuilder of(String name, String... fields) {
		this.name = name;
		this.fields = fields;
		return this;
	}
	
	public ElasticMultiTermsAggregationBuilder of(String name, String field) {
		this.name = name;
		this.fields = new String[]{field};
		return this;
	}
	
	public ElasticMultiTermsAggregationBuilder of(String name, List<String> fields) {
		Objects.requireNonNull(fields, "fields cannot be null!");
		this.name = name;
		this.fields = fields.stream().toArray(String[]::new);
		return this;
	}
	
	/**
	 * 这个是限制返回桶的数量, 如果总共有10个桶, 但是size设为5, 那么聚合结果中只会返回前5个桶
	 *
	 * @param size
	 * @return ElasticMultiTermsAggregationBuilder
	 */
	public ElasticMultiTermsAggregationBuilder size(Integer size) {
		this.size = size;
		return this;
	}
	
	/**
	 * 帮助解决Terms不准的问题<br/>
	 * Terms 聚合分析不准的原因, 数据分散在多个不同的分片上, Coordinating Node 无法获取数据全貌<br/>
	 * 解决方案 1: 当数据量不大时, 设置Primary Shard为1; 实现准确性<br/>
	 * 方案 2: 在分布式数据上, 设置shard_size参数, 提高精确度<br/>
	 * 原理: 每次从Shard上额外多获取数据, 提升准确率
	 *
	 * @param shardSize
	 * @return ElasticTermsAggregationBuilder
	 */
	public ElasticMultiTermsAggregationBuilder shardSize(Integer shardSize) {
		this.shardSize = shardSize;
		return this;
	}
	
	/**
	 * 聚合返回的结果中是否要包含总命中数 
	 * @param fetchTotalHits
	 * @return ElasticMultiTermsAggregationBuilder
	 */
	public ElasticMultiTermsAggregationBuilder fetchTotalHits(boolean fetchTotalHits) {
		this.fetchTotalHits = fetchTotalHits;
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(name);
		if (size != null) {
			aggregationBuilder.size(size);
		}
		if (shardSize != null) {
			aggregationBuilder.shardSize(shardSize);
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("fields", fields);
		//ElasticUtils.Cluster.createMultiFieldAgg 创建的stored script名字是这个: multi_fields
		Script painless = new Script(ScriptType.STORED, null, "multi_fields", params);
		aggregationBuilder.script(painless);
		
		SubAggregationSupport.addSubAggregations(aggregationBuilder, subAggregations);
		if (!sortOrders.isEmpty()) {
			if (sortOrders.size() == 1) {
				aggregationBuilder.order(sortOrders.get(0).toBucketOrder());
			} else {
				aggregationBuilder.order(sortOrders.stream().map(SortOrder::toBucketOrder).collect(Collectors.toList()));
			}
		}
		return aggregationBuilder;
	}
	
	@Override
	public ElasticCompositeAggregationBuilder and() {
		compositeAggregationBuilder.add(build());
		return compositeAggregationBuilder;
	}
	
	public <T> List<Map<String, T>> get() {
		TermsAggregationBuilder arrregationBuilder = (TermsAggregationBuilder) build();
		
		SearchRequestBuilder searchRequestBuilder = searchRequestBuilder();
		
		SearchRequestBuilder builder = searchRequestBuilder
				.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		SearchResponse searchResponse = builder.get();
		addTotalHitsToThreadLocal(searchResponse);
		Aggregations aggregations = searchResponse.getAggregations();
		
		return AggResultSupport.termsResult(aggregations);
	}
	
	public ElasticPage getPage() {
		TermsAggregationBuilder arrregationBuilder = (TermsAggregationBuilder) build();
		
		SearchRequestBuilder searchRequestBuilder = searchRequestBuilder();
		
		SearchRequestBuilder builder = searchRequestBuilder
				.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		SearchResponse searchResponse = builder.get();
		addTotalHitsToThreadLocal(searchResponse);
		Aggregations aggregations = searchResponse.getAggregations();
		
		List<Map<String, Object>> results = AggResultSupport.termsResult(aggregations);
		
		ElasticPage elasticPage = ElasticPage.<Map<String, Object>>builder()
				.results(results)
				.build();
		elasticPage.setPageSize(page.getPageSize());
		elasticPage.setCurrentPage(page.getCurrentPage());
		return elasticPage;
	}
	
	@Override
	public ElasticMultiTermsAggregationBuilder subAggregation(SubAggregation subAggregation) {
		if (subAggregation instanceof ElasticBucketSortSubAggregation) {
			this.page = ((ElasticBucketSortSubAggregation)subAggregation).toPage();
		}
		subAggregations.add(subAggregation);
		return this;
	}
	
	public ElasticMultiTermsAggregationBuilder sort(String sort) {
		List<SortOrder> sortOrders = SortSupport.sort(sort);
		this.sortOrders.addAll(sortOrders);
		return this;
	}
}
