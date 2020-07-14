package com.loserico.java8.stream.grouping;

import com.loserico.java8.stream.grouping.model.Person;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import static java.util.stream.Collectors.groupingBy;

/**
 * Conclusion 
 * 
 * The Java 8 Collectors API provides us with an easy way to group our
 * collections. By nesting collectors, we can add different layers of groups to
 * implement multi level groupings.
 * 
 * @on
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-05-06 11:33
 * @version 1.0
 *
 */
public class Group extends AbstractGroup {

	public static void main(String[] args) {
		Group example = new Group();

		List<Person> persons = buildPersonsList();

		example.singleLevelGrouping(persons);
		example.twoLevelGrouping(persons);
		example.threeLevelGrouping(persons);
	}

	/*
	 * Single level grouping 
	 * 
	 * The simplest form of grouping is the single level
	 * grouping. In this example we are going to group all persons in the collection
	 * by their country:
	 * @on
	 */
	public void singleLevelGrouping(List<Person> persons) {
		Map<String, List<Person>> personsByCountry = persons.stream()
				.collect(groupingBy(Person::getCountry));

		System.out.println("Persons in USA: " + personsByCountry.get("USA"));
	}

	/*
	 * Two level grouping 
	 * 
	 * In this example, we will group not only by country but also
	 * by city. To accomplish this, we need to implement a two level grouping. We will
	 * group persons by country and for each country, we will group its persons by the
	 * city where they live. In order to allow multi level grouping, the groupingBy
	 * method in class Collectors supports an additional Collector as a second
	 * argument:
	 * @on
	 */
	public void twoLevelGrouping(List<Person> persons) {
		final Map<String, Map<String, List<Person>>> personsByCountryAndCity = persons.stream().collect(
				groupingBy(Person::getCountry,
						groupingBy(Person::getCity)));
		System.out.println("Persons living in London: " + personsByCountryAndCity.get("UK").get("London").size());
	}

	/*
	 * Three level grouping 
	 * In our final example, we will take a step further and
	 * group people by country, city and pet name. I have splitted it into two methods
	 * for readability:
	 * @on
	 */
	public void threeLevelGrouping(List<Person> persons) {
		final Map<String, Map<String, Map<String, List<Person>>>> personsByCountryCityAndPetName = persons.stream()
				.collect(
						groupingBy(Person::getCountry,
								groupByCityAndPetName()));
		System.out.println("Persons whose pet is named 'Max' and live in NY: " +
				personsByCountryCityAndPetName.get("USA").get("NYC").get("Max").size());
	}

	private Collector<Person, ?, Map<String, Map<String, List<Person>>>> groupByCityAndPetName() {
		return groupingBy(Person::getCity, groupingBy(p -> p.getPet().getName()));
	}
}