package com.loserico.search;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.util.List;

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
public class MatchQueryTest {
	
	@Test
	public void testMatchQuery() {
		List<String> results = ElasticUtils.query("movies")
				.queryBuilder(QueryBuilders.matchQuery("title", "Man of the Year")
						//.operator(Operator.AND))
						.minimumShouldMatch("3"))
				.queryForList();
		results.forEach(System.out::println);
	}
}
