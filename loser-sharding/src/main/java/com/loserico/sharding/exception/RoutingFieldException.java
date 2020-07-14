package com.loserico.sharding.exception;

import com.loserico.sharding.enumeration.RoutingErrors;
import lombok.Data;

/**
 * 入参中没有包含路由字段异常 
 * <p>
 * Copyright: Copyright (c) 2020-02-14 18:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class RoutingFieldException extends RoutingException {
	
	public RoutingFieldException(RoutingErrors errors) {
		super(errors.getMsg());
		setErrorCode(errors.getCode());
		setErrorMsg(errors.getMsg());
	}
	
}