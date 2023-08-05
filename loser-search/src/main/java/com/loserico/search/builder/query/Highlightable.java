package com.loserico.search.builder.query;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

/**
 * <p>
 * Copyright: (C), 2023-08-03 20:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface Highlightable {
	
	public HighLight highlight(String field);
	
	public HighlightBuilder toHighlightBuilder();
}
