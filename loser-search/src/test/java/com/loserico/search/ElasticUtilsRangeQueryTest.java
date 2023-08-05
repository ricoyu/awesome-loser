package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2023-08-01 10:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsRangeQueryTest {
	
	@Test
	public void testRangeQuery() {
		List<Object> products = ElasticUtils.Query
				.range("products")
				.field("price")
				.lte(30)
				.gt(20)
				.constantScore(true)
				.queryForList();
		assertThat(products.size() == 2);
	}
	
	@Test
	public void testDateRange() {
		List<Object> products = ElasticUtils.Query
				.range("products")
				.field("date")
				.gt("now-5y")
				.queryForList();
		assertThat(products.size() ==1);
	}
}
