package com.loserico.search;

import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-06-06 11:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ConstantScoreQueryTest {
	
	@Test
	public void testTermConstantScore() {
		List<Object> iphones = ElasticUtils.Query.termQuery("products")
				.query("productID.keyword", "XHDK-A-1293-#fJ3")
				.constantScore(true)
				.includeSources("desc")
				.queryForList();
		
		iphones.forEach(System.out::println);
	}
	
	@Test
	public void testMatchConstantScore() {
		List<Object> iphones = ElasticUtils.Query.matchQuery("products")
				.query("desc", "iPhone")
				.constantScore(true)
				.includeSources("desc")
				.queryForList();
		
		iphones.forEach(System.out::println);
	}
}
