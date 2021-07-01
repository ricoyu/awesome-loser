package com.loserico.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Copyright: (C), 2021-05-10 13:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggResult {
	
	/**
	 * bucket大部分情况是字符串, 但是也可能对一个Long类型字段做聚合, 此时的Bucket就是long
	 */
	private Object bucket;
	
	private Object count;
	
}
