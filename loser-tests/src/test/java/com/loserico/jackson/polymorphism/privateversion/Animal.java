package com.loserico.jackson.polymorphism.privateversion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({@Type(value = Lion.class, name = "lion"), @Type(value = Elephant.class, name = "elephant")})
@Data
public abstract class Animal {
	
	private String name;
	
	private String type;
	
	private String sound;
	
	private boolean endangered;
	
}
