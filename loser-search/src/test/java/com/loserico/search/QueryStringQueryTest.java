package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
		List<String> indices = ElasticUtils.Admin.listIndices();
		indices.forEach(System.out::println);
	}
	
}
