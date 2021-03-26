package com.loserico.searchlegacy.support;

import com.loserico.searchlegacy.enums.Direction;
import lombok.Data;

/**
 * 排序规则
 * <p>
 * Copyright: (C), 2020-12-30 9:25
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public final class Sort {
	
	/**
	 * 要排序的字段
	 */
	private String field;
	
	/**
	 * 排序规则
	 */
	private Direction direction;
	
	public static Sort of(String field, Direction direction) {
		Sort sort = new Sort();
		sort.setField(field);
		sort.setDirection(direction);
		return sort;
	}
}
