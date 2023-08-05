package com.loserico.search.builder.query;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;

/**
 * <p>
 * Copyright: (C), 2023-08-03 20:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface HighLight {
	
	public HighLight preTags(String... preTags);
	public String[] preTags();
	
	public HighLight postTags(String... postTags);
	
	public String[] postTags();
	
	public BaseQueryBuilder andThen();
	
	public Field  toHighlightField();
}
