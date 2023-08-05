package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2023-08-01 22:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsBoolQueryTest {
	
	@Test
	public void testBoolMust() {
		List<Object> movies = ElasticUtils.Query
				.bool("newmovies")
				.term("genre.keyword", "Comedy").must()
				.term("genre_count", 1).must()
				.queryForList();
		assertEquals(movies.size(), 1);
	}
	
	@Test
	public void testBoolFilter() {
		List<Object> movies = ElasticUtils.Query
				.bool("newmovies")
				.term("genre.keyword", "Comedy").filter()
				.term("genre_count", 1).filter()
				.queryForList();
		assertEquals(movies.size(), 1);
				
	}
	
	@Test
	public void testBoolQueryOptimize() {
		List<Object> news = ElasticUtils.Query
				.bool("news")
				.match("content", "apple").must()
				.match("content", "pie").mustNot()
				.queryForList();
		 assertEquals(2, news.size());
	}
	
	@Test
	public void testBoolBoostingQuery() {
	}
}
