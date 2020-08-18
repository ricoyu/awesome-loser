package com.loserico.jackson.polymorphism.privateversion;

import lombok.Data;

@Data
public class Elephant extends Animal {

	private String name;

	public Elephant(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getSound() {
		return "trumpet";
	}

	public String getType() {
		return "herbivorous";
	}

	public boolean isEndangered() {
		return false;
	}

	@Override
	public String toString() {
		return "Elephant [name=" + name + ", getName()=" + getName() + ", getSound()=" + getSound() + ", getType()=" + getType()
				+ ", isEndangered()=" + isEndangered() + "]";
	}

}