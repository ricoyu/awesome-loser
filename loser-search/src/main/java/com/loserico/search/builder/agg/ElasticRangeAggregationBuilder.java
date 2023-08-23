package com.loserico.search.builder.agg;

import com.loserico.search.builder.agg.sub.SubAggregation;
import com.loserico.search.builder.agg.sub.SubAggregationSupport;
import com.loserico.search.builder.agg.support.FromToRange;
import com.loserico.search.builder.agg.support.KeyFromToRange;
import com.loserico.search.builder.agg.support.KeyUnboundFromRange;
import com.loserico.search.builder.agg.support.KeyUnboundToRange;
import com.loserico.search.builder.agg.support.Range;
import com.loserico.search.builder.agg.support.UnboundFromRange;
import com.loserico.search.builder.agg.support.UnboundToRange;
import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.support.AggResultSupport;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-aggregations-bucket-datehistogram-aggregation.html
 * <p>
 * Copyright: (C), 2021-07-12 18:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticRangeAggregationBuilder extends AbstractAggregationBuilder implements ElasticAggregationBuilder, SubAggregatable, Compositable {
	
	private List<Range> ranges = new ArrayList<>();
	
	/**
	 * Add a new range to this aggregation.
	 *
	 * @param key  the key to use for this range in the response
	 * @param from the lower bound on the distances, inclusive
	 * @param to   the upper bound on the distances, exclusive
	 */
	public ElasticRangeAggregationBuilder addRange(String key, double from, double to) {
		if (isBlank(key)) {
			ranges.add(new FromToRange(from, to));
		} else {
			ranges.add(new KeyFromToRange(key, from, to));
		}
		return this;
	}
	
	/**
	 * Same as {@link #addRange(String, double, double)} but the key will be
	 * automatically generated based on <code>from</code> and
	 * <code>to</code>.
	 */
	public ElasticRangeAggregationBuilder addRange(double from, double to) {
		return addRange(null, from, to);
	}
	
	/**
	 * Add a new range with no lower bound.
	 *
	 * @param key the key to use for this range in the response
	 * @param to  the upper bound on the distances, exclusive
	 */
	public ElasticRangeAggregationBuilder addUnboundedTo(String key, double to) {
		if (isNotBlank(key)) {
			ranges.add(new KeyUnboundToRange(key, to));
		} else {
			ranges.add(new UnboundToRange(to));
		}
		return this;
	}
	
	/**
	 * Same as {@link #addUnboundedTo(String, double)} but the key will be
	 * computed automatically.
	 */
	public ElasticRangeAggregationBuilder addUnboundedTo(double to) {
		return addUnboundedTo(null, to);
	}
	
	/**
	 * Add a new range with no upper bound.
	 *
	 * @param key  the key to use for this range in the response
	 * @param from the lower bound on the distances, inclusive
	 */
	public ElasticRangeAggregationBuilder addUnboundedFrom(String key, double from) {
		if (isNotBlank(key)) {
			ranges.add(new KeyUnboundFromRange(key, from));
		} else {
			ranges.add(new UnboundFromRange(from));
		}
		return this;
	}
	
	/**
	 * Same as {@link #addUnboundedFrom(String, double)} but the key will be
	 * computed automatically.
	 */
	public ElasticRangeAggregationBuilder addUnboundedFrom(double from) {
		return addUnboundedFrom(null, from);
	}
	
	
	private ElasticRangeAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticRangeAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticRangeAggregationBuilder(indices);
	}
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @param field
	 * @return ElasticHistogramAggregationBuilder
	 */
	@Override
	public ElasticRangeAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	@Override
	public ElasticRangeAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	/**
	 * 聚合返回的结果中是否要包含总命中数
	 *
	 * @param fetchTotalHits
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticRangeAggregationBuilder fetchTotalHits(boolean fetchTotalHits) {
		this.fetchTotalHits = fetchTotalHits;
		return this;
	}
	
	@Override
	public ElasticRangeAggregationBuilder subAggregation(SubAggregation subAggregation) {
		subAggregations.add(subAggregation);
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		RangeAggregationBuilder aggregationBuilder = AggregationBuilders.range(name).field(field);
		for (Range range : ranges) {
			range.addRange(aggregationBuilder);
		}
		SubAggregationSupport.addSubAggregations(aggregationBuilder, subAggregations);
		return aggregationBuilder;
	}
	
	@Override
	public ElasticCompositeAggregationBuilder and() {
		compositeAggregationBuilder.add(build());
		return compositeAggregationBuilder;
	}
	
	@SuppressWarnings({"unchecked"})
	public <T> Map<String, T> get() {
		RangeAggregationBuilder arrregationBuilder = (RangeAggregationBuilder) build();
		
		SearchRequestBuilder searchRequestBuilder = searchRequestBuilder();
		
		SearchRequestBuilder builder = searchRequestBuilder
				.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		SearchResponse searchResponse = builder.get();
		addTotalHitsToThreadLocal(searchResponse);
		Aggregations aggregations = searchResponse.getAggregations();
		
		return (Map<String, T>) AggResultSupport.rangeResult(aggregations);
	}
	
}
