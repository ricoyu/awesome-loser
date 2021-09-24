package com.loserico.search;

import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-06-30 17:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TermsQueryTest {
	
	@Test
	public void testTermsQuery() {
		List<Object> movies = ElasticUtils.Query.termsQuery("movies")
				.query("title.keyword", "Balto", "Mortal Kombat")
				.queryForList();
		
		movies.forEach(System.out::println);
	}
	
	
}
