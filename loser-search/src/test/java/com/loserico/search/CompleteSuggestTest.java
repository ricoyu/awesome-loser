package com.loserico.search;

import com.loserico.search.enums.FieldType;
import com.loserico.search.support.BulkResult;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2023-08-05 11:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CompleteSuggestTest {
	
	@Test
	public void testCompletionSuggestion() throws InterruptedException {
		ElasticUtils.Admin.deleteIndex("articles");
		boolean created = ElasticUtils.Admin.createIndex("articles")
				.mapping()
				.field("title_completion", FieldType.COMPLETION)
				.thenCreate();
		Assert.assertTrue(created);

		BulkResult bulkResult = ElasticUtils.bulkIndex("articles",
				"{\"title_completion\": \"lucene is very cool\"}",
				"{\"title_completion\": \"Elasticsearch builds on top of Lucene\"}",
				"{\"title_completion\": \"Elasticsearch rocks\"}",
				"{\"title_completion\": \"elastic is the company behind ELK stack\"}",
				"{\"title_completion\": \"TLK stack rocks\"}");
		SECONDS.sleep(1);
		
		CompletionSuggestionBuilder suggestionBuilder = SuggestBuilders.completionSuggestion("title_completion").prefix("e");
		Set<String> suggests = ElasticUtils.suggest("articles")
				.suggestionBuilder(suggestionBuilder)
				.name("title_completion")
				.suggest();
		
		suggests.forEach(System.out::println);
		System.out.println("-----------------");
		
		suggests = ElasticUtils.completionSuggest("e", "title_completion", "articles");
		suggests.forEach(System.out::println);
		
	}
}
