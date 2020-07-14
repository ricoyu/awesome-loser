package com.loserico.java8.stream.grouping.model;

public class Person {
	private final String name;
	private final String country;
	private final String city;
	private final Pet pet;

	public Person(String name, String country, String city, Pet pet) {
		this.name = name;
		this.country = country;
		this.city = city;
		this.pet = pet;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public Pet getPet() {
		return pet;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", country='" + country + '\'' +
				", city='" + city + '\'' +
				'}';
	}
}