package com.loserico.search.support;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2023-08-15 12:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Builder
public class StatsAggResult {
	
	private String name;
	
	private long count;
	
	private double min;
	
	private double max;
	
	private double sum;
}
