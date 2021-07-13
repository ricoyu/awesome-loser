package com.loserico.search.enums;

import static com.loserico.search.enums.AggCategory.BUCKET;
import static com.loserico.search.enums.AggCategory.METRIC;

/**
 * 聚合的类型, 如terms, histogram, avg...
 * <p>
 * Copyright: (C), 2021-07-12 18:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum AggType {
	
	TERMS(BUCKET),
	
	HISTOGRAM(BUCKET),
	
	COMPOSITE(BUCKET),
	
	AVG(METRIC),
	
	CARDINALITY(METRIC),
	
	MAX(METRIC),
	
	MIN(METRIC),
	
	SUM(METRIC);
	
	
	private AggCategory category;
	
	private AggType(AggCategory category) {
		this.category = category;
	}
}
