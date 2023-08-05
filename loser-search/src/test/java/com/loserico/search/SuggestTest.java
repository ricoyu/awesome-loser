package com.loserico.search;

import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.junit.Test;

import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2023-08-05 10:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SuggestTest {
	
	@Test
	public void testTermSuggester() {
		TermSuggestionBuilder suggestionBuilder =
				SuggestBuilders.termSuggestion("body")
						.suggestMode(TermSuggestionBuilder.SuggestMode.POPULAR)
						.text("lucen rock");
		//.prefixLength(1)
		//.stringDistance(INTERNAL)
		//.sort(FREQUENCY);
		
		Set<String> suggesters = ElasticUtils.suggest("articles")
				.name("term-suggestion")
				.suggestionBuilder(suggestionBuilder)
				.suggest();
		
		suggesters.forEach(System.out::println);
	}
	
	
	@Test
	public void testPhraseSuggester() {
		PhraseSuggestionBuilder suggestionBuilder = SuggestBuilders.phraseSuggestion("body")
				.text("lucne and elasticsear rock")
				.maxErrors(2f)
				.confidence(2)
				.highlight("<em>", "</em>");
		Set<String> suggests = ElasticUtils.suggest("articles")
				.suggestionBuilder(suggestionBuilder)
				.name("phrase-suggestion")
				.suggest();
		
		suggests.forEach(System.out::println);
	}
}
