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
	
	private String bucket;
	
	private Object count;
	
}
