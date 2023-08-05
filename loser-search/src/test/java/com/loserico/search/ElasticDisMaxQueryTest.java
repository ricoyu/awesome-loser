package com.loserico.search;

import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.junit.Test;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * <p>
 * Copyright: (C), 2023-08-02 15:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticDisMaxQueryTest {
	
	@Test
	public void testDisMaxQuery() {
		DisMaxQueryBuilder queryBuilder = disMaxQuery()
				.tieBreaker(0.2f)
				.add(matchQuery("title", "Quick pets"))
				.add(matchQuery("body", "Quick pets"));
		List<Object> blogs = ElasticUtils.Query
				.query("blogs")
				.queryBuilder(queryBuilder)
				.queryForList();
		blogs.forEach(System.out::println);
	}
}
