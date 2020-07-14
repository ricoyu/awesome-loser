package com.loserico.java8.stream.grouping;

import com.loserico.java8.stream.grouping.model.Person;
import com.loserico.java8.stream.grouping.model.Pet;
import com.loserico.json.jackson.JacksonUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;

public class MultiLevelGroupingTest {

	private List<Person> persons;

	@Before
	public void setup() {
		Person person1 = new Person("John", "USA", "NYC", new Pet("Max", 5));
		Person person2 = new Person("Steve", "UK", "London", new Pet("Lucy", 8));
		Person person3 = new Person("Anna", "USA", "NYC", new Pet("Buddy", 12));
		Person person4 = new Person("Mike", "USA", "Chicago", new Pet("Duke", 10));

		persons = asList(person1, person2, person3, person4);
	}

	/**
	 * Grouping by and transform 
	 * 
	 * Let’s take the model I used in the previous post where we had a collection of persons who owned a pet. 
	 * Now we want to know which pets belong to persons living in New York. We are asking for pets, so we can’t
	 * just make a grouping since we would be returning a collection of persons. What
	 * we need to do is group persons by city and then transform the stream to a
	 * collection of pets. For this purpose, we use mapping on the result of the group by
	 * 
	 * In the grouping phase, we group persons by city and then perform a mapping to get each person’s pet.
	 * 
	 * @on
	 */
	@Test
	public void testGroupAndTransform() {
		Map<String, List<Pet>> petsGroupedByCity = persons.stream()
				.collect(groupingBy(
						Person::getCity,
						mapping(Person::getPet, toList())));
		System.out.println(JacksonUtils.toJson(petsGroupedByCity));
		System.out.println("Pets living in New York: " + petsGroupedByCity.get("NYC"));
	}

	/**
	 * The previous example is useful for converting groupings of objects, but maybe
	 * we don’t want to obtain the whole list for each group. In this example, we
	 * still want to group pets by its owner’s city, but this time we only want to get
	 * the oldest pet of each list.
	 * 
	 * After we group persons by city, in collectingAndThen we are transforming each
	 * person in each city’s list to its pet, and then applying a reduction to get the
	 * pet with the highest age in the list.
	 * 
	 * Conclusion
	 * 
	 * Collectors API not only allow us to group collections of things but also make transformations 
	 * and reductions to obtain different objects depending on our needs.
	 * 
	 * @on
	 */
	@Test
	public void testGroupingTransformingReducing() {
		Map<String, Pet> olderPetOfEachCity = persons.stream().collect(
				groupingBy(
						Person::getCity,
						collectingAndThen(
								mapping(
										Person::getPet,
										maxBy((pet1, pet2) -> Integer.compare(pet1.getAge(), pet2.getAge()))),
								Optional::get)));

		Map<String, Pet> olderPetOfEachCity2 = persons.stream().collect(
				groupingBy(
						Person::getCity,
						collectOlderPet()));

		System.out.println("The older pet living in New York is: " + olderPetOfEachCity.get("NYC"));
	}

	private Collector<Person, ?, Pet> collectOlderPet() {
		return Collectors.collectingAndThen(
				mapping(
						Person::getPet,
						Collectors.maxBy((pet1, pet2) -> Integer.compare(pet1.getAge(), pet2.getAge()))),
				Optional::get);
	}
}
