package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static com.loserico.json.jsonpath.JsonPathUtils.ifExists;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-06-06 14:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ExistsQueryTest {
	
	@Test
	public void testDateExists() {
		List<String> products = ElasticUtils.Query.exists("products")
				.field("date")
				.queryForList();
		
		products.forEach(System.out::println);
		products.forEach((product) -> {
			assertTrue(ifExists(product, "$.date"));
		});
	}
	
	@Test
	public void testDateExistsInclude() {
		List<String> products = ElasticUtils.Query.exists("products")
				.field("date")
				.includeSources("date", "price")
				.queryForList();
		
		products.forEach(System.out::println);
		products.forEach((product) -> {
			assertTrue(ifExists(product, "$.date"));
		});
	}
}
