package com.loserico.search.builder.query;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2023-08-03 20:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MultiMatchHighlight implements HighLight {
	
	private ElasticMultiMatchQueryBuilder queryBuilder;
	private String field;
	
	private String[] preTags;
	
	private String[] postTags;
	
	public MultiMatchHighlight(ElasticMultiMatchQueryBuilder queryBuilder, String field) {
		this.queryBuilder = queryBuilder;
		this.field = field;
	}
	
	@Override
	public MultiMatchHighlight preTags(String... preTags) {
		this.preTags = preTags;
		return this;
	}
	
	@Override
	public MultiMatchHighlight postTags(String... postTags) {
		this.postTags = postTags;
		return this;
	}
	
	@Override
	public String[] preTags() {
		return this.preTags;
	}
	
	@Override
	public String[] postTags() {
		return this.postTags;
	}
	
	@Override
	public ElasticMultiMatchQueryBuilder andThen() {
		return queryBuilder;
	}
	
	@Override
	public Field toHighlightField() {
		if (isBlank(field) ) {
			throw new IllegalArgumentException("field and pre/post tags must be set in MultiMatchHighlight");
		}
		
		Field highlightName = new Field(field);
		
		return highlightName;
	}
}
