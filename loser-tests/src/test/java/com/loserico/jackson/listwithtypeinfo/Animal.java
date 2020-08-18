package com.loserico.jackson.listwithtypeinfo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2020-08-14 14:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({@JsonSubTypes.Type(value = Lion.class, name = "lion"), @JsonSubTypes.Type(value = Elephant.class, name = "elephant")})
public abstract class Animal {
	
	private String name;
	
}
