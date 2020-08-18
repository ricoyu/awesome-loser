package com.loserico.jackson.listwithtypeinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class Zoo {
	
	private String name;
	
	private String city;
	
	private List<Animal> animals = new ArrayList<Animal>();
	
	@JsonCreator
	public Zoo(@JsonProperty("name") String name, @JsonProperty("city") String city) {
		this.name = name;
		this.city = city;
	}
	
	public List<Animal> addAnimal(Animal animal) {
		animals.add(animal);
		return animals;
	}
}
