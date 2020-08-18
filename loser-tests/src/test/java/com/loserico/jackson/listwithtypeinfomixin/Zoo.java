package com.loserico.jackson.listwithtypeinfomixin;

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
	
	public Zoo(String name, String city) {
		this.name = name;
		this.city = city;
	}
	
	public List<Animal> addAnimal(Animal animal) {
		animals.add(animal);
		return animals;
	}
}
