package com.loserico.jackson.polymorphism.publicversion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

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
public class Zoo {
	
	public String name;
	
	public String city;
	
	public List<Animal> animals;
	
	public Zoo(String name, String city) {
		this.name = name;
		this.city = city;
	}
	
	public void setAnimals(List animals) {
		this.animals = animals;
	}
	
	@Override
	public String toString() {
		return "Zoo [name=" + name + ", city=" + city + ", animals=" + animals + "]";
	}
}
