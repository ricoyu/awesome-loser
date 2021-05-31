package com.loserico.search;

import com.loserico.search.ElasticUtils.Query;
import com.loserico.search.exception.UriQueryException;
import com.loserico.search.pojo.Movie;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-04-29 11:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsUriQueryTest {
	
	@Test
	public void testQueryReturnString() {
		List<String> movies = Query.uriQuery("movies")
				.query("title:2012")
				.queryForList();
		movies.forEach(System.out::println);
	}
	
	@Test
	public void testUriQuery() {
		List<String> movies = Query.uriQuery("movies")
				.query("title:2012")
				.queryForList();
		
		assertThat(movies.size()).isEqualTo(2);
		movies.forEach(System.out::println);
		
		List<Movie> moviePojos = Query.uriQuery("movies")
				.query("2012")
				.field("title")
				.resultType(Movie.class)
				.queryForList();
		assertEquals(movies.size(), moviePojos.size());
		moviePojos.forEach(System.out::println);
	}
	
	@Test
	public void testUriOr() {
		//查询title包含Beautiful 或者 Mind
		List<Object> movies = Query.uriQuery("movies")
				.query("title:Beautiful Mind")
				.queryForList();
		
		movies.forEach(System.out::println);
		
		List<Object> movies2 = Query.uriQuery("movies")
				.query("title:Beautiful OR Mind")
				.queryForList();
		
		assertEquals(movies.size(), movies2.size());
	}
	
	@Test(expected = UriQueryException.class)
	public void testSortThenException() {
		List<Object> movies = Query.uriQuery("movies")
				.query("title:Beautiful Mind")
				.sort("title:asc")
				.queryForList();
	}
	
	@Test
	public void testSortThenOK() {
		List<Object> movies = Query.uriQuery("movies")
				.query("title:Beautiful Mind")
				.sort("title.keyword:asc")
				.queryForList();
		movies.forEach(System.out::println);
	}
	
	@Test
	public void testEmptyQueryString() {
		List<Object> movies = Query.uriQuery("movies")
				.query("")
				.field("title")
				.sort("title:asc")
				.queryForList();
		movies.forEach(System.out::println);
	}
	
	@Test
	public void testQueryEscape() {
		List<Object> movies = Query.uriQuery("movies")
				.query("title:(Beautiful +Mind)")
				.sort("title.keyword:asc")
				.queryForList();
		movies.forEach(System.out::println);
		
		movies = Query.uriQuery("movies")
				.query("title:(Beautiful -Mind)")
				.sort("title.keyword:asc")
				.queryForList();
		movies.forEach(System.out::println);
	}
	
	@Test
	public void testRange() {
		List<Object> movies = Query.uriQuery("movies")
				.query("year:[2002 TO 2004]")
				.sort("year:asc")
				.size(1000)
				.queryForList();
		movies.forEach(System.out::println);
	}
	
	@Test
	public void testRegex() {
		List<Object> movies = Query.uriQuery("movies")
				.query("title:[bt]oy")
				.sort("title.keyword")
				.size(1000)
				.queryForList();
		movies.forEach(System.out::println);
	}

}
