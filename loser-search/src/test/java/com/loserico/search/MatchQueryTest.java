package com.loserico.search;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2021-01-13 9:03
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class MatchQueryTest {
	
	@Test
	public void testMatchQuery() {
		List<String> results = ElasticUtils.Query.query("movies")
				.queryBuilder(QueryBuilders.matchQuery("title", "Man of the Year")
						//.operator(Operator.AND))
						.minimumShouldMatch("3"))
				.exclideSources("title")
				.queryForList();
		results.forEach(System.out::println);
	}
	
	@Test
	public void testOperatorAnd() {
		List<Object> titles = ElasticUtils.Query.matchQuery("movies")
				.query("title", "Last Christmas")
				.includeSources("title")
				.queryForList();
		titles.forEach(System.out::println);
		
		titles = ElasticUtils.Query.matchQuery("movies")
				.query("title", "Last Christmas")
				.operator(Operator.AND)
				.includeSources("title")
				.queryForList();
		titles.forEach(System.out::println);
	}
	
	@Test
	public void testPainless() {
		SearchRequestBuilder req = ElasticUtils.client.prepareSearch("kibana_sample_data_ecommerce");
		req.setQuery(QueryBuilders.matchAllQuery());
		req.addScriptField("my_field", new Script("doc['email'].value"));
		
		log.debug("Query DSL:\n{}", new JSONObject(req.toString()).toString(2));
		
		SearchResponse resp = req.get();
		for (SearchHit hit : resp.getHits().getHits()) {
			Map<String, DocumentField> fields = hit.getFields();
			System.out.println(fields.get("my_field"));
		}
		
	}
	
	@Test
	public void testPainLess2() {
		Map<String, List<Object>> resultMap = ElasticUtils.Query.matchQuery("kibana_sample_data_ecommerce")
				.scriptField("loser_field", "'rico_' + doc['email'].value")
				.sort("email:asc")
				.queryForScriptFields();
		resultMap.get("loser_field").forEach(System.out::println);
	}
}
