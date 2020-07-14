package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeName;

public class Zoo {
	public Zoo() {
	}

	public Animal animal;

	public Zoo(Animal animal) {
		this.animal = animal;
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
			include = As.PROPERTY,
			property = "type")
	@JsonSubTypes({
			@JsonSubTypes.Type(value = Dog.class, name = "dog"),
			@JsonSubTypes.Type(value = Cat.class, name = "cat")
	})
	public static class Animal {
		public String name;
	}

	@JsonTypeName("dog")
	public static class Dog extends Animal {
		public double barkVolume;

		public Dog(double barkVolume) {
			this.barkVolume = barkVolume;
		}

		public Dog(String name) {
			super.name = name;
		}
	}

	@JsonTypeName("cat")
	public static class Cat extends Animal {
		boolean likesCream;
		public int lives;

		public Cat(boolean likesCream, int lives) {
			this.likesCream = likesCream;
			this.lives = lives;
		}

		public Cat(String name) {
			super.name = name;
		}

	}
}