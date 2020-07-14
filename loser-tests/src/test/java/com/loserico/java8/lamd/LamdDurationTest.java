package com.loserico.java8.lamd;

import com.loserico.common.lang.enums.Gender;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class LamdDurationTest {

	/**
	 * https://dzone.com/articles/lambdas-and-clean-code?edition=327499&utm_source=Daily%20Digest&utm_medium=email&utm_campaign=Daily%20Digest%202017-09-26
	 */
	@Test
	public void testDuration() {
		List<Person> persons = asList(
				new Person("rico", Gender.MALE, LocalDate.of(1982, 11, 9)),
				new Person("James", Gender.FEMALE, LocalDate.of(2000, 11, 2)),
				new Person("Peter", Gender.FEMALE, LocalDate.of(1997, 10, 25)));

		List<String> audits = persons.stream().filter((p) -> {
			if (p.getGender() == Gender.MALE) {
				return true;
			}
			LocalDate now = LocalDate.now();
			Duration age = Duration.between(p.getBirthday(), now);
			Duration audit = Duration.of(18, ChronoUnit.YEARS);
			if (age.compareTo(audit) > 0) {
				return true;
			}

			return false;
		}).map(Person::getName)
				.collect(toList());

		audits.forEach(System.out::println);

	}
}
