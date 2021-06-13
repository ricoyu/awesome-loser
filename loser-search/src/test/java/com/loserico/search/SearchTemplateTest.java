package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-06-11 11:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SearchTemplateTest {
	
	@Test
	public void testCreateSearchTemplate() {
		boolean created = ElasticUtils.Admin.createSearchTemplate("tmdb_template", "scripts/tmdb_template.mustache");
		assertTrue(created);
	}
	
	@Test
	public void testDeleteSearchTemplate() {
		boolean deleted = ElasticUtils.Admin.deleteSearchTemplate("tmdb_template");
		assertTrue(deleted);
	}
	
	@Test
	public void testTemplateQuery() {
		List<Object> results = ElasticUtils.Query.templateQuery("tmdb")
				.templateName("tmdb_template")
				.param("q", "basketball")
				.queryForList();
		
		results.forEach(System.out::println);
	}
	
	@Test
	public void testCreateDynamicSearchTemplate() {
		boolean created = ElasticUtils.Admin.createSearchTemplate("employee_template", "scripts/employee_template.mustache");
		assertTrue(created);
	}
	
	@Test
	public void testDynamicSearchTemplate() {
		List<Object> results = ElasticUtils.Query.templateQuery("employee")
				.templateName("employee_template")
				.param("job", "java")
				.param("age", 38)
				.queryForList();
		results.forEach(System.out::println);
	}
}
