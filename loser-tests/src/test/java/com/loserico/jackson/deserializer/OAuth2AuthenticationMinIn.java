package com.loserico.jackson.deserializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用来处理没有默认构造函数的对象
 * <p>
 * Copyright: (C), 2020/4/29 18:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OAuth2AuthenticationMinIn {
	
	@JsonCreator
	public OAuth2AuthenticationMinIn(@JsonProperty("name") String name, @JsonProperty("age") Integer age) {
		
	}
}
