package com.loserico.search;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticUtils.Aggs;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.json.jackson.JacksonUtils.toPrettyJson;
import static org.assertj.core.api.Assertions.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;

/**
 * <p>
 * Copyright: (C), 2021-05-10 10:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticAggsTest {
	
	/**
	 * 统计每种颜色的汽车数量
	 */
	@Test
	public void testAggRaw() {
		TermsAggregationBuilder arrregationBuilder = AggregationBuilders.terms("popular_colors")
				.field("color.keyword");
		SearchResponse searchResponse = ElasticUtils.client.prepareSearch("cars")
				.addAggregation(arrregationBuilder)
				.get();
		Aggregations aggregations = searchResponse.getAggregations();
		
		for (Aggregation aggregation : aggregations) {
			Map<String, Object> metaData = aggregation.getMetaData();
			String name = aggregation.getName();
			System.out.println("Aggregation: " + name);
			List<Bucket> buckets = ((StringTerms) aggregation).getBuckets();
			for (Bucket bucket : buckets) {
				String key = bucket.getKeyAsString();
				long docCount = bucket.getDocCount();
				System.out.println("Key: " + key + ", Doc Count: " + docCount);
			}
		}
	}
	
	@Test
	public void testAgg() {
		Map<String, Object> aggResults = Aggs.terms("cars")
				.of("cars-color", "color.keyword")
				.size(5)
				.get();
		assertThat(aggResults.size()).isEqualTo(3);
		System.out.println(toPrettyJson(aggResults));
		
		Map<String, Object> aggResults1 = Aggs.terms("kibana_sample_data_flights")
				.of("dest-country", "DestCountry")
				.get();
		System.out.println(toPrettyJson(aggResults1));
	}
	
	/**
	 * 查看航班目的地的统计信息, 增加平均, 最高最低价格
	 */
	@Test
	public void testFlightTermsMinMaxAvg() {
		TermsAggregationBuilder termsAggregationBuilder = terms("flight_dest").field("DestCountry")
				.subAggregation(avg("avg_price").field("AvgTicketPrice"))
				.subAggregation(max("max_price").field("AvgTicketPrice"))
				.subAggregation(min("min_price").field("AvgTicketPrice"));
		
		SearchResponse response = ElasticUtils.client.prepareSearch("kibana_sample_data_flights")
				.setSize(0)
				.addAggregation(termsAggregationBuilder)
				.get();
		
		Aggregations aggregations = response.getAggregations();
		for (Aggregation aggregation : aggregations) {
			System.out.println("Aggregation: " + aggregation.getName());
			List<Bucket> buckets = ((StringTerms) aggregation).getBuckets();
			for (Bucket bucket : buckets) {
				String key = bucket.getKeyAsString();
				long docCount = bucket.getDocCount();
				System.out.println("Key: " + key + ", Doc Count: " + docCount);
				
				Aggregations subAggs = bucket.getAggregations();
				if (subAggs != null) {
					for (Aggregation subAgg : subAggs) {
						System.out.println(toJson(subAgg));
						String name = subAgg.getName(); //max_price
						String writeableName = ((NamedWriteable) subAgg).getWriteableName(); //max min 等聚合的类型
						Object value = ReflectionUtils.getFieldValue(writeableName, subAgg);
						if (writeableName.equals("avg")) {
							value = ReflectionUtils.invokeMethod(subAgg, "getValue");
						}
						System.out.println(writeableName + ":" + value);
					}
				}
			}
		}
	}
	
	@Test
	public void testHistogramAgg() {
		Map<String, Object> resultMap = Aggs.histogram("employees")
				.of("salary_histogram", "salary")
				.interval(5000)
				.get();
		
		System.out.println(toPrettyJson(resultMap));
	}
	
	@Test
	public void testHistogramAggWithMinDocCountAndExtendedBounds() {
		//long min = 1625642342501L - 5 * 60 * 60 * 1000L;
		long min = 1625642342501L;
		long max = 1625642350168L;
		
		log.info("Min {}", new Date(min));
		log.info("Max {}", new Date(max));
		
		ElasticRangeQueryBuilder rangeQueryBuilder = ElasticUtils.Query.range("event_2021-07-07")
				.field("create_time")
				.gte(min)
				.lte(max);
		
		Map<String, Object> resultMap = Aggs.histogram("event_2021-07-07")
				.of("event_count_agg", "create_time")
				.setQuery(rangeQueryBuilder)
				.interval(2000)
				.minDocCount(0)
				//.extendedBounds(min - 10 * 1000, max)
				.get();
		
		log.info(toPrettyJson(resultMap));
	}
	
	@Test
	public void testSubAgg() {
		Map<String, Object> resultMap = Aggs.terms("kibana_sample_data_flights")
				.of("flight_dest", "DestCountry")
				.get();
		
		System.out.println(toPrettyJson(resultMap));
	}
}
