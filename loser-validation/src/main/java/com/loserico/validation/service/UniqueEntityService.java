package com.loserico.validation.service;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-08-17 17:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface UniqueEntityService {
	
	/**
	 * 根据传入的params查询记录数
	 * @param params
	 * @return int
	 */
	public int count(Map<String, Object> params, Serializable pk);
	
}
