package com.loserico.jackson.polymorphism.privateversion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import lombok.Data;

import java.util.List;

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
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = As.PROPERTY, property = "@class")
@Data
public class Zoo {
	
	private String name;
	
	private String city;
	
	private List<Animal> animals;
	
	public Zoo(String name, String city) {
		this.name = name;
		this.city = city;
	}
	
	@Override
	public String toString() {
		return "Zoo [name=" + name + ", city=" + city + ", animals=" + animals + "]";
	}
}
