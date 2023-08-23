package com.loserico.search.builder.agg.support;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2023-08-17 11:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Builder
public class RangeAggResult {
	
	private String key;
	long docCount;
	Object from;
	Object to;
}
