package com.loserico.jackson.polymorphism.privateversion;

import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2020-08-14 14:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class Lion extends Animal {
	
	private String name;
	
	public Lion(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSound() {
		return "Roar";
	}
	
	public String getType() {
		return "carnivorous";
	}
	
	public boolean isEndangered() {
		return true;
	}
	
	@Override
	public String toString() {
		return "Lion [name=" + name + ", getName()=" + getName() + ", getSound()=" + getSound() + ", getType()=" + getType() + ", isEndangered()="
				+ isEndangered() + "]";
	}
}
