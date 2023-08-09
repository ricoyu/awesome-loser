package com.loserico.search;

import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-08-09 12:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsSortTest {
	
	@Test
	public void testSingleFieldSort() {
		List<Object> docs = ElasticUtils.Query.matchAllQuery("kibana_sample_data_ecommerce")
				.sort("order_date:desc,_score:desc, _doc:asc")
				.includeSources("order_date", "_score", "_doc")
				.size(5)
				.queryForList();
		
		docs.forEach(System.out::println);
	}
}
