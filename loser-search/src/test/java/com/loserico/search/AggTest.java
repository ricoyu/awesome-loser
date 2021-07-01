package com.loserico.search;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.builder.query.ElasticTermQueryBuilder;
import com.loserico.search.vo.AggResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toPrettyJson;

/**
 * 聚合分析
 * <p>
 * Copyright: (C), 2021-06-03 9:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class AggTest {
	
	@Test
	public void testAggOnDestContry() {
		List<AggResult> aggResults = ElasticUtils.Aggs.terms("kibana_sample_data_flights")
				.of("flight_dest", "DestCountry")
				.get();
		
		aggResults.forEach(agg -> System.out.println(toPrettyJson(agg)));
	}
	
	@Test
	public void testSubAggregate() {
		List<AggResult> aggResults = ElasticUtils.Aggs.terms("kibana_sample_data_flights")
				.of("flight_dest", "DestCountry")
				.get();
		aggResults.forEach(System.out::println);
	}
	
	@Test
	public void testCardinalityAgg() {
		Long count = ElasticUtils.Aggs.cardinality("employees")
				.of("cardinality_agg", "job.keyword")
				.get();
		
		System.out.println(count);
	}
	
	@Test
	public void testMin() {
		Double minSalary = ElasticUtils.Aggs.min("employees")
				.of("min_salary", "salary")
				.get();
		log.info("Min Salary: {}", minSalary);
	}
	
	@Test
	public void testMax() {
		Double maxSalary = ElasticUtils.Aggs.max("employees")
				.of("max_salary", "salary")
				.get();
		log.info("Max Salary: {}", maxSalary);
	}
	
	@Test
	public void testAvg() {
		Double avgSalary = ElasticUtils.Aggs.avg("employees")
				.of("avg_salary", "salary")
				.get();
		log.info("Avg Salary: {}", avgSalary);
	}
	
	@Test
	public void testSum() {
		Double sum = ElasticUtils.Aggs.sum("employees")
				.of("sum_agg", "age")
				.get();
		System.out.println(sum);
	}
	
	@Test
	public void testComposite() {
		Map<String, Object> result = ElasticUtils.Aggs.composite("employees")
				.min("min_salary", "salary")
				.max("max_salary", "salary")
				.avg("avg_salary", "salary")
				.terms("term_agg", "age").and()
				.get();
		
		System.out.println(toPrettyJson(result));
	}
	
	@Test
	public void testAggAfterQuery() {
		ElasticTermQueryBuilder elasticTermQueryBuilder = ElasticUtils.Query.termQuery("employees").query("job", "java").size(0);
		QueryBuilder queryBuilder = ReflectionUtils.invokeMethod(elasticTermQueryBuilder, "builder");
		SearchResponse searchResponse = ElasticUtils.client.prepareSearch("employees")
				.setQuery(queryBuilder)
				.addAggregation(AggregationBuilders.stats("stats_agg").field("salary"))
				.get();
		
		Aggregation aggregation = searchResponse.getAggregations().get("stats_agg");
		System.out.println(aggregation);
	}
	
	@Test
	public void testQueryThenAgg() {
		ElasticRangeQueryBuilder rangeQueryBuilder = ElasticUtils.Query.range("employees").field("age").gt(20);
		List<AggResult> aggResults = ElasticUtils.Aggs.terms("employees")
				.of("jobs", "job.keyword")
				.size(10)
				.setQuery(rangeQueryBuilder)
				.get();
		
		aggResults.forEach(System.out::println);
	}
}
