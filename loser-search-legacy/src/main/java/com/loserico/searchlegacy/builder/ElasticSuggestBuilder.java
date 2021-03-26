package com.loserico.searchlegacy.builder;

import com.loserico.searchlegacy.ElasticUtils;
import com.loserico.searchlegacy.enums.SuggestMode;
import com.loserico.searchlegacy.enums.SuggestSort;
import com.loserico.searchlegacy.exception.SuggestException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.Suggest.Suggestion;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2021-02-13 20:55
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ElasticSuggestBuilder {
	
	private String[] indices;
	
	/**
	 * 给这个suggestion起一个名字
	 */
	private String name;
	
	private SuggestionBuilder suggestionBuilder;
	
	/**
	 * The suggest text is a required option that needs to be set globally or per suggestion.
	 */
	private String text;
	
	/**
	 * The field to fetch the candidate suggestions from.
	 * This is a required option that either needs to be set globally or per suggestion.
	 */
	private String field;
	
	/**
	 * The number of minimal prefix characters that must match in order be a candidate for suggestions. Defaults to 1.
	 * Increasing this number improves spellcheck performance. Usually misspellings don’t occur in the beginning of terms.
	 */
	private int prefixLength = 1;
	
	/**
	 * 按照score还是文档的frequency来排序
	 */
	private SuggestSort sort = SuggestSort.FREQUENCY;
	
	private SuggestMode suggestMode = SuggestMode.MISSING;
	
	public ElasticSuggestBuilder(String... indices) {
		this.indices = indices;
	}
	
	/**
	 * 给这个suggestion起一个名字
	 *
	 * @param name
	 * @return ElasticSuggestBuilder
	 */
	public ElasticSuggestBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * 可以通过SuggestBuilders提供的工厂类来提供
	 *
	 * @param suggestionBuilder
	 * @return ElasticSuggestBuilder
	 */
	public ElasticSuggestBuilder suggestionBuilder(SuggestionBuilder suggestionBuilder) {
		this.suggestionBuilder = suggestionBuilder;
		return this;
	}
	
	public Set<String> suggest() {
		if (isBlank(name)) {
			throw new SuggestException("Every suggestion need a name!");
		}
		SuggestBuilder suggestBuilder = new SuggestBuilder();
		suggestBuilder.addSuggestion(name, suggestionBuilder);
		if (log.isDebugEnabled()) {
			log.debug("Suggest DSL:\n {}", suggestBuilder.toString());
		}
		SearchResponse searchResponse = ElasticUtils.client
				.prepareSearch(indices)
				.suggest(suggestBuilder)
				.get();
		
		Suggest suggest = searchResponse.getSuggest();
		Set<String> suggesters = new HashSet<>();
		
		/*
		 * 如果文档加入索引后立刻suggest, 可能搜索不到, 这时候suggest会是null
		 */
		if (suggest == null) {
			return suggesters;
		}
		
		Suggestion suggestion = suggest.getSuggestion(name);
		/*
		 * 处理TermSuggestion
		 */
		if (suggestion instanceof TermSuggestion) {
			List<TermSuggestion.Entry> entries = ((TermSuggestion) suggestion).getEntries();
			for (TermSuggestion.Entry entry : entries) {
				entry.getOptions().forEach(option -> suggesters.add(option.getText().string()));
			}
			return suggesters;
		}
		
		/*
		 * 处理PhraseSuggestion
		 */	
		if (suggestion instanceof PhraseSuggestion) {
			List<PhraseSuggestion.Entry> entries = ((PhraseSuggestion) suggestion).getEntries();
			for (PhraseSuggestion.Entry entry : entries) {
				entry.getOptions().forEach(option -> suggesters.add(option.getHighlighted().string()));
			}
			return suggesters;
		}
		
		/*
		 * 处理CompletionSuggestion
		 */
		if (suggestion instanceof CompletionSuggestion) {
			List<CompletionSuggestion.Entry> entries = ((CompletionSuggestion) suggestion).getEntries();
			for (CompletionSuggestion.Entry entry : entries) {
				entry.getOptions().forEach(option -> suggesters.add(option.getText().string()));
			}
			return suggesters;
		}
		
		return suggesters;
	}
}
