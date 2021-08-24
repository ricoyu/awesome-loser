package com.loserico.search.builder.agg.sub;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;

import java.util.Objects;

/**
 * <p>
 * Copyright: (C), 2021-08-23 14:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElsticHistogramSubAggregation extends ElasticSubAggregation implements AvgSubAggregation {
	
	private ElasticSubAggregation parentAggregation;
	
	/**
	 * 聚合名字
	 */
	protected String name;
	
	/**
	 * 要对哪个字段聚合
	 */
	protected String field;
	
	protected double interval;
	
	/**
	 * min_doc_count: 0 <br/>
	 * 就是说如果在interval指定的隔间内, 某个区间没有值, 在返回的结果中, 这个区间要不要包含? <br/>
	 * 强制返回所有 buckets，即使 buckets 可能为空
	 *
	 * <pre> {@code
	 * {
	 *   "key": 100.0,
	 *   "doc_count": 0
	 * }
	 * }</pre>
	 * <p>
	 * 上面的结果只有在min_doc_count: 0时才会返回, 如果我们要返回区间内有值的, 可以设置 min_doc_count: 1
	 */
	protected Integer minDocCount;
	
	/**
	 * extended_bounds.min
	 * <p>
	 * min_doc_count 参数强制返回空 buckets，但是 Elasticsearch 默认只返回你的数据中最小值和最大值之间的 buckets。
	 * <p>
	 * 假设你的数据只落在了 4 月和 7 月之间，那么你只能得到这些月份的 buckets（可能为空也可能不为空）。因此为了得到全年数据，
	 * 我们需要告诉 Elasticsearch 我们想要全部 buckets， 即便那些 buckets 可能落在最小日期 之前 或 最大日期 之后 。
	 */
	protected Long minBound;
	
	/**
	 * extended_bounds.max
	 * <p>
	 * min_doc_count 参数强制返回空 buckets，但是 Elasticsearch 默认只返回你的数据中最小值和最大值之间的 buckets。
	 * <p>
	 * 假设你的数据只落在了 4 月和 7 月之间，那么你只能得到这些月份的 buckets（可能为空也可能不为空）。因此为了得到全年数据，
	 * 我们需要告诉 Elasticsearch 我们想要全部 buckets， 即便那些 buckets 可能落在最小日期 之前 或 最大日期 之后 。
	 */
	protected Long maxBound;
	
	public ElsticHistogramSubAggregation(ElasticSubAggregation parentAggregation, String name, String field) {
		this.parentAggregation = parentAggregation;
		this.name = name;
		this.field = field;
	}
	
	public ElsticHistogramSubAggregation(String name, String field) {
		this.parentAggregation = parentAggregation;
		this.name = name;
		this.field = field;
	}
	
	public ElsticHistogramSubAggregation interval(double interval) {
		this.interval = interval;
		return this;
	}
	
	/**
	 * min_doc_count: 0 <br/>
	 * 就是说如果在interval指定的隔间内, 某个区间没有值, 在返回的结果中, 这个区间要不要包含?
	 *
	 * <pre> {@code
	 * {
	 *   "key": 100.0,
	 *   "doc_count": 0
	 * }
	 * }</pre>
	 * <p>
	 * 上面的结果只有在min_doc_count: 0时才会返回, 如果我们要返回区间内有值的, 可以设置 min_doc_count: 1
	 *
	 * @param minDocCount
	 * @return
	 */
	public ElsticHistogramSubAggregation minDocCount(Integer minDocCount) {
		this.minDocCount = minDocCount;
		return this;
	}
	
	/**
	 * min_doc_count 参数强制返回空 buckets，但是 Elasticsearch 默认只返回你的数据中最小值和最大值之间的 buckets。
	 * <p>
	 * 因此如果你的数据只落在了 4 月和 7 月之间，那么你只能得到这些月份的 buckets（可能为空也可能不为空）。因此为了得到全年数据，
	 * 我们需要告诉 Elasticsearch 我们想要全部 buckets， 即便那些 buckets 可能落在最小日期 之前 或 最大日期 之后 。
	 *
	 * <pre> {@code
	 * "extended_bounds" : {
	 *     "min" : "2014-01-01",
	 *     "max" : "2014-12-31"
	 * }
	 * }</pre>
	 *
	 * @param minBound
	 * @param maxBound
	 * @return SubHistogramAgg
	 */
	public ElsticHistogramSubAggregation extendedBounds(Long minBound, Long maxBound) {
		Objects.requireNonNull(minBound, "minBound cannot be null!");
		Objects.requireNonNull(maxBound, "maxBound cannot be null!");
		this.minBound = minBound;
		this.maxBound = maxBound;
		return this;
	}
	
	@Override
	public ElsticHistogramSubAggregation avgSubAggregation(String name, String field) {
		ElasticAvgSubAggregation elasticAvgSubAggregation = new ElasticAvgSubAggregation(this, name, field);
		subAggregations.add(elasticAvgSubAggregation);
		return this;
	}
	
	public AggregationBuilder build() {
		HistogramAggregationBuilder aggregationBuilder = AggregationBuilders.histogram(name)
				.field(field)
				.interval(this.interval);
		
		if (minDocCount != null) {
			aggregationBuilder.minDocCount(minDocCount);
		}
		
		if (minDocCount != null && maxBound != null) {
			aggregationBuilder.extendedBounds(minBound, maxBound);
		}
		
		subAggregations.forEach(subAggregation -> aggregationBuilder.subAggregation(subAggregation.build()));
		
		return aggregationBuilder;
	}
	
	@Override
	public ElasticSubAggregation and() {
		return parentAggregation;
	}
}
