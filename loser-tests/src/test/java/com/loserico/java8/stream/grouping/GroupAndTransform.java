package com.loserico.java8.stream.grouping;

import com.loserico.java8.stream.grouping.model.Person;
import com.loserico.java8.stream.grouping.model.Pet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class GroupAndTransform extends AbstractGroup {

	public static void main(String[] args) {
		GroupAndTransform example = new GroupAndTransform();

		List<Person> persons = buildPersonsList();

		example.groupAndTransform(persons);
		example.groupTransformAndReduce(persons);
	}

	//Group pets by their owner's city
	public void groupAndTransform(List<Person> persons) {
		final Map<String, List<Pet>> petsGroupedByCity = persons.stream().collect(
				groupingBy(
						Person::getCity,
						mapping(Person::getPet, toList())));

		System.out.println("Pets living in New York: " + petsGroupedByCity.get("NYC"));
	}

	//Get older pet of each city
	public void groupTransformAndReduce(List<Person> persons) {
		final Map<String, Pet> olderPetOfEachCity = persons.stream().collect(
				groupingBy(
						Person::getCity,
						collectOlderPet()));

		System.out.println("The older pet living in New York is: " + olderPetOfEachCity.get("NYC"));
	}

	private Collector<Person, ?, Pet> collectOlderPet() {
		return collectingAndThen(
				mapping(
						Person::getPet,
						Collectors.maxBy((pet1, pet2) -> Integer.compare(pet1.getAge(), pet2.getAge()))),
				Optional::get);
	}
}