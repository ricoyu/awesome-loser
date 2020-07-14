package com.loserico.sharding.exception;

import static com.loserico.sharding.enumeration.RoutingErrors.FORMAT_TABLE_SUFFIX_ERROR;

/**
 * <p>
 * Copyright: (C), 2020/2/14 18:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FormatTableSuffixException extends RoutingException {
	
	public FormatTableSuffixException() {
		super(FORMAT_TABLE_SUFFIX_ERROR.getMsg());
		setErrorCode(FORMAT_TABLE_SUFFIX_ERROR.getCode());
		setErrorMsg(FORMAT_TABLE_SUFFIX_ERROR.getMsg());
	}
	
}
