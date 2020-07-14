package com.loserico.common.lang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 排序
 * <p>
 * Copyright: Copyright (c) 2019-10-14 15:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认的排序的数据库字段名
	 */
	private String orderBy = "create_time";

	/**
	 * 正序还是倒序
	 */
	private ORDER_BY direction = ORDER_BY.DESC;

	public enum ORDER_BY {
		/**
		 * 升序
		 */
		ASC,

		/**
		 * 降序
		 */
		DESC
	}
}
