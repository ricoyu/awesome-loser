package com.loserico.search;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;

import java.util.List;

import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.BEST_FIELDS;
import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.CROSS_FIELDS;
import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.MOST_FIELDS;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * <p>
 * Copyright: (C), 2023-08-02 15:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticMultiMatchTest {
	
	@Test
	public void testMostFields() {
		MultiMatchQueryBuilder queryBuilder = multiMatchQuery("barking dogs", "title", "title.std")
				.type(MOST_FIELDS);
		List<Object> titles = ElasticUtils.Query
				.query("titles")
				.queryBuilder(queryBuilder)
				.queryForList();
		titles.forEach(System.out::println);
	}
	
	@Test
	public void testCrossFields() {
		MultiMatchQueryBuilder queryBuilder = multiMatchQuery("Poland Street W1V", "street", "city", "country", "postcode")
				.operator(Operator.AND)
				.type(CROSS_FIELDS);
		
		new HighlightBuilder.Field("title").preTags("<span style=\\\"color:red\\\">").postTags("</span>");
		List<Object> addresses = ElasticUtils.Query
				.query("address")
				.queryBuilder(queryBuilder)
				.queryForList();
		
		addresses.forEach(System.out::println);
	}
	
	@Test
	public void testMultiMatchWithHighlight() {
		List<Object> movies = ElasticUtils.Query
				.multiMatch("tmdb")
				.query("basketball with cartoon aliens", "title^10", "overview")
				.size(1)
				.highlight("title")
				.preTags("<em>")
				.postTags("</em>")
				.andThen()
				.highlight("overview")
				.preTags("<em>")
				.postTags("</em>")
				.andThen()
				.includeSources("title", "overview")
				.queryForList();
		
		movies.forEach(System.out::println);
	}

	@Test
	public void testProducts() {
		List<Object> products = ElasticUtils.Query.multiMatch("products")
				.type(BEST_FIELDS)
				.tieBreaker(0.3f)
				.query("apple watch", "title", "description")
				.queryForList();
		products.forEach(System.out::println);
	}

	@Test
	public void testMostFields2() {
		List<Object> news = ElasticUtils.Query.multiMatch("news")
				.type(MOST_FIELDS)
				.query("climate change impact", "title", "intro", "body")
				.queryForList();
		news.forEach(System.out::println);
	}

	@Test
	public void testCrossFields2() {
		List<Object> address = ElasticUtils.Query.multiMatch("address")
				.type(CROSS_FIELDS)
				.query("Poland Street W1V", "street", "city", "country", "postcode")
				.operator(Operator.AND)
				.queryForList();
		address.forEach(System.out::println);
	}
}
