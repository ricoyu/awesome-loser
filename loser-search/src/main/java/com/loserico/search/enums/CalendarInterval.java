package com.loserico.search.enums;

/**
 * Histogram 聚合时的calendar_interval 单位
 * <p>
 * Copyright: (C), 2021-07-14 9:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum CalendarInterval {
	
	MINUTE("1m"),
	
	HOUR("1h"),
	
	DAY("1d"),
	
	WEEK("1w"),
	
	MONTH("1M"),
	
	/**
	 * 季度
	 */
	QUARTER("1q"),
	
	YEAR("1y");
	
	private String shotcut;
	
	private CalendarInterval(String shotcut) {
		this.shotcut = shotcut;
	}
	
	public String shotcut() {
		return this.shotcut;
	}
}
