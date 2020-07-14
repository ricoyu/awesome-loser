package com.loserico.validation.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 错误描述类 
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:45
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail implements Serializable {

	private String errorMessage;
	private String rejectedValue;
	private String propertyPath;

}
