package com.loserico.search;

import com.loserico.search.pojo.Movie;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2021-05-30 12:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class QueryStringQueryTest {
	
	@Test
	public void test() {
		List<String> users = ElasticUtils.Query.queryString("users")
				.query("name:ruan")
				.queryForList();
		users.forEach(System.out::println);
	}
	
	@Test
	public void testAndThenEmpty() {
		List<String> users = ElasticUtils.Query.queryString("users")
				.query("about:(java AND lua)")
				.queryForList();
		assertThat(users).isEmpty();
		users.forEach(System.out::println);
	}
	
	@Test
	public void testPhrase() {
		List<String> users = ElasticUtils.Query.queryString("users")
				.query("name:\"ruan yiming\"")
				.queryForList();
		users.forEach(System.out::println);
	}
	
	@Test
	public void testDefaultField() {
		List<String> users = ElasticUtils.Query.queryString("users")
				.query("ruan AND yiming")
				.defaultField("name")
				.queryForList();
		users.forEach(System.out::println);
	}
	
	@Test
	public void testGroupQuery() {
		List<String> users = ElasticUtils.Query.queryString("users")
				.fields("name", "about")
				.query("(ruan AND yiming) OR (Java AND Elasticsearch)")
				.queryForList();
		
		users.forEach(System.out::println);
	}
	
	@Test
	public void testPlusMinus() {
		List<String> users = ElasticUtils.Query.queryString("users")
				.query("name:(+yiming +ruan)")
				.queryForList();
		users.forEach(System.out::println);
	}
	
	@Test
	public void testNotOr() {
		long count = ElasticUtils.Query.queryString("netlog_*")
				.query("dst_port:((NOT 80) OR 10050)")
				.queryForCount();
		System.out.println(count);
	}
	
	@Test
	public void testListIndices() {
		List<String> indices = ElasticUtils.Admin.listIndexNames();
		indices.forEach(System.out::println);
	}

	@Test
	public void testQueryAll() {
		List<Object> products = ElasticUtils.Query.queryString("product")
				.queryForList();
		products.forEach(System.out::println);
	}

	@Test
	public void testQuery4Name() {
		List<Object> products = ElasticUtils.Query.queryString("product")
				.query("name:nfc phone")
				.queryForList();
		products.forEach(System.out::println);
	}

	@Test
	public void testGroupQuery2() {
		//List<Movie> movies = ElasticUtils.Query.queryString("movies")
				//.query("title:(Beautiful Mind)")
				//.query("title:(Beautiful AND Mind)")
				//.query("title:(Beautiful NOT Mind)")
				//.query("year:>=1980")
				//.includeSources("year")
				//.sort("year:asc")
				//.resultType(Movie.class)
				//.queryForList();
		List<Movie> movies1 = ElasticUtils.Query.uriQuery("movies")
				.query("year:>=1980")
				.sort("year:asc")
				.resultType(Movie.class)
				.queryForList();
		//assertThat(movies.size()).isEqualTo(movies1.size());
		for (Movie movie : movies1) {
			System.out.println(movie.getYear());
		}
		//System.out.println(movies.size());
		//for (Movie movie : movies) {
		//	System.out.println(movie.getYear());
		//}
	}
}
