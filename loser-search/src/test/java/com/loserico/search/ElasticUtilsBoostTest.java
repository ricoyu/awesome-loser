package com.loserico.search;

import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-08-02 10:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsBoostTest {
	
	@Test
	public void testBoost() {
		List<Object> blogs = ElasticUtils.Query
				.bool("blogs")
				.match("title", "apple,ipad").boost(4).should()
				.match("content", "apple,ipad").should()
				.queryForList();
		blogs.stream().forEach(System.out::println);
	}
}
