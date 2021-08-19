package com.loserico.search.enums;

/**
 * Histogram聚合时的Fixed interval
 * <p>
 * Copyright: (C), 2021-07-14 9:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum FixedInterval {
	
	SECONDS("s"),
	
	MINUTES("m"),
	
	HOURS("h"),
	
	DAYS("d");
	
	private String shotcut;
	
	private FixedInterval(String shotcut) {
		this.shotcut = shotcut;
	}
	
	public String shotcut() {
		return this.shotcut;
	}
}
