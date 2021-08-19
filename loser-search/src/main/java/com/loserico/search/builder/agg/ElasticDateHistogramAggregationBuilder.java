package com.loserico.search.builder.agg;

import com.loserico.common.lang.constants.DateConstants;
import com.loserico.search.builder.agg.sub.DateHistogramSubAvgAgg;
import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.enums.CalendarInterval;
import com.loserico.search.enums.FixedInterval;
import com.loserico.search.support.AggResultSupport;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static com.loserico.common.lang.utils.Assert.notNull;
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
public class ElasticDateHistogramAggregationBuilder extends AbstractAggregationBuilder implements ElasticAggregationBuilder, SubAggable, Compositable {
	
	/**
	 * 可以识别 夏令时, 不同月份有不同的天数, 特定年份的润秒
	 */
	private DateHistogramInterval calendarInterval;
	
	/**
	 * 就是固定的时间间隔, 不管上面说的日历上的差异
	 */
	private DateHistogramInterval fixedInterval ;
	
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
	private Integer minDocCount;
	
	/**
	 * extended_bounds.min
	 * <p>
	 * min_doc_count 参数强制返回空 buckets，但是 Elasticsearch 默认只返回你的数据中最小值和最大值之间的 buckets。
	 * <p>
	 * 假设你的数据只落在了 4 月和 7 月之间，那么你只能得到这些月份的 buckets（可能为空也可能不为空）。因此为了得到全年数据，
	 * 我们需要告诉 Elasticsearch 我们想要全部 buckets， 即便那些 buckets 可能落在最小日期 之前 或 最大日期 之后 。
	 */
	private Long minBound;
	
	/**
	 * extended_bounds.max
	 * <p>
	 * min_doc_count 参数强制返回空 buckets，但是 Elasticsearch 默认只返回你的数据中最小值和最大值之间的 buckets。
	 * <p>
	 * 假设你的数据只落在了 4 月和 7 月之间，那么你只能得到这些月份的 buckets（可能为空也可能不为空）。因此为了得到全年数据，
	 * 我们需要告诉 Elasticsearch 我们想要全部 buckets， 即便那些 buckets 可能落在最小日期 之前 或 最大日期 之后 。
	 */
	private Long maxBound;
	
	/**
	 * 提供日期格式以便 buckets 的键值便于阅读
	 */
	private String format;
	
	/**
	 * 上面格式化成日期字符串时使用的时区
	 */
	private ZoneId timezone;
	
	/**
	 * 为Terms聚合添加的子聚合
	 */
	private List<AggregationBuilder> subAggBuilders = new ArrayList<>();
	
	private ElasticDateHistogramAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticDateHistogramAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticDateHistogramAggregationBuilder(indices);
	}
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @param field
	 * @return ElasticHistogramAggregationBuilder
	 */
	@Override
	public ElasticDateHistogramAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	/**
	 * 就是固定的时间间隔, 不识别 夏令时, 不同月份有不同的天数, 特定年份的润秒
	 * @param n
	 * @param interval
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder fixedInterval(Integer n, FixedInterval interval) {
		notNull(n, "n cannot be null!");
		notNull(interval, "interval cannot be null!");
		switch (interval) {
			case SECONDS:
				this.fixedInterval = DateHistogramInterval.seconds(n);
				break;
			case MINUTES:
				this.fixedInterval = DateHistogramInterval.minutes(n);
				break;
			case HOURS:
				this.fixedInterval = DateHistogramInterval.hours(n);
				break;
			case DAYS:
				this.fixedInterval = DateHistogramInterval.days(n);
				break;
		}
		return this;
	}
	
	/**
	 * 可以识别 夏令时, 不同月份有不同的天数, 特定年份的润秒
	 * @param interval
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder calendarInterval(CalendarInterval interval) {
		notNull(interval, "interval cannot be null!");
		switch (interval) {
			case MINUTE:
				this.calendarInterval = DateHistogramInterval.MINUTE;
				break;
			case HOUR:
				this.calendarInterval = DateHistogramInterval.HOUR;
				break;
			case DAY:
				this.calendarInterval = DateHistogramInterval.DAY;
				break;
			case WEEK:
				this.calendarInterval = DateHistogramInterval.WEEK;
				break;
			case MONTH:
				this.calendarInterval = DateHistogramInterval.MONTH;
				break;
			case QUARTER:
				this.calendarInterval = DateHistogramInterval.QUARTER;
				break;
			case YEAR:
				this.calendarInterval = DateHistogramInterval.YEAR;
				break;
		}
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
	public ElasticDateHistogramAggregationBuilder minDocCount(Integer minDocCount) {
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
	 * @return ElasticHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder extendedBounds(Long minBound, Long maxBound) {
		Objects.requireNonNull(minBound, "minBound cannot be null!");
		Objects.requireNonNull(maxBound, "maxBound cannot be null!");
		this.minBound = minBound;
		this.maxBound = maxBound;
		return this;
	}
	
	/**
	 * 提供日期格式以便 buckets 的键值便于阅读
	 * @param format
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder format(String format) {
		notNull(format, "format cannot be null!");
		this.format = format;
		return this;
	}
	
	/**
	 * 设置转日期字符串时使用的时区, 默认Asia/Shanghai
	 * @param zoneId
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder timezone(String zoneId) {
		notNull(zoneId, "zoneId cannot be null!");
		this.timezone = ZoneId.of(zoneId);
		return this;
	}
	
	/**
	 * 设置转日期字符串时使用的时区, 默认Asia/Shanghai
	 * @param zoneId
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder timezone(ZoneId zoneId) {
		notNull(zoneId, "zoneId cannot be null!");
		this.timezone = zoneId;
		return this;
	}
	
	/**
	 * 设置转日期字符串时使用的时区, 默认Asia/Shanghai
	 * @param timeZone
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder timezone(TimeZone timeZone) {
		notNull(timeZone, "timeZone cannot be null!");
		this.timezone = timeZone.toZoneId();
		return this;
	}
	
	@Override
	public ElasticDateHistogramAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	/**
	 * 聚合返回的结果中是否要包含总命中数 
	 * @param fetchTotalHits
	 * @return ElasticDateHistogramAggregationBuilder
	 */
	public ElasticDateHistogramAggregationBuilder fetchTotalHits(boolean fetchTotalHits) {
		this.fetchTotalHits = fetchTotalHits;
		return this;
	}
	
	@Override
	public DateHistogramSubAvgAgg subAvg(String name, String field) {
		return new DateHistogramSubAvgAgg(this, name, field);
	}
	
	@Override
	public AggregationBuilder build() {
		DateHistogramAggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(name).field(field);
		if (fixedInterval != null) {
			aggregationBuilder.fixedInterval(fixedInterval);
		}
		if (calendarInterval != null) {
			aggregationBuilder.calendarInterval(calendarInterval);
		}
		if (minDocCount != null) {
			aggregationBuilder.minDocCount(minDocCount);
		}
		
		if (minBound != null && maxBound != null) {
			aggregationBuilder.extendedBounds(new ExtendedBounds(minBound, maxBound));
		}
		
		if (isNotBlank(format)) {
			aggregationBuilder.format(format);
		}
		if (timezone != null) {
			aggregationBuilder.timeZone(timezone);
		} else {
			aggregationBuilder.timeZone(DateConstants.CHINA.toZoneId());
		}
		subAggBuilders.forEach(subAgg -> aggregationBuilder.subAggregation(subAgg));
		return aggregationBuilder;
	}
	
	@Override
	public ElasticCompositeAggregationBuilder and() {
		compositeAggregationBuilder.add(build());
		return compositeAggregationBuilder;
	}
	
	@SuppressWarnings({"unchecked"})
	public <T> Map<String, T> get() {
		DateHistogramAggregationBuilder arrregationBuilder = (DateHistogramAggregationBuilder) build();
		
		SearchRequestBuilder searchRequestBuilder = searchRequestBuilder();
		
		SearchRequestBuilder builder = searchRequestBuilder
				.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		SearchResponse searchResponse = builder.get();
		addTotalHitsToThreadLocal(searchResponse);
		Aggregations aggregations = searchResponse.getAggregations();
		
		return (Map<String, T>) AggResultSupport.dateHistogramResult(aggregations);
	}
	
	private void subAggregation(AggregationBuilder subAggregationBuilder) {
		Objects.requireNonNull(subAggregationBuilder, "subAggregationBuilder cannot be null!");
		subAggBuilders.add(subAggregationBuilder);
	}
}
