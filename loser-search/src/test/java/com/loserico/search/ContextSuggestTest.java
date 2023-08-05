package com.loserico.search;

import org.junit.Test;

import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2023-08-05 16:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ContextSuggestTest {
	
	@Test
	public void testContextSuggestion() {
		Set<String> suggests = ElasticUtils.contextSuggest("comments")
				.name("contextSuggestName")
				.category("movies")
				.categoryName("comment_category")
				.field("comment_autocomplete")
				.prefix("sta")
				.suggest();
		
		suggests.forEach(System.out::println);
	}
	
	@Test
	public void testContextSuggestCoffee() {
		Set<String> suggests = ElasticUtils.contextSuggest("comments")
				.name("my_suggestion")
				.prefix("sta")
				.category("movies")
				.field("comment_autocomplete")
				.categoryName("comment_category")
				.suggest();
		
		suggests.forEach(System.out::println);
	}
}
