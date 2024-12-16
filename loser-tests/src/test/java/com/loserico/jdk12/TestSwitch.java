package com.loserico.jdk12;

import org.junit.Test;

public class TestSwitch {

	@Test
	public void test() {
		var month = Month.JANUARY;
		var season = "";

		switch (month) {
			case JANUARY, FEBRUARY, MARCH -> season = "Winter";
			case APRIL, MAY, JUNE -> season = "Spring";
			case JULY, AUGUST, SEPTEMBER -> season = "Summer";
			case OCTOBER, NOVEMBER, DECEMBER -> season = "Autumn";
			default -> throw new RuntimeException("Invalid month");
		}

		System.out.println(season);

	}

	@Test
	public void test2() {
		var month = Month.JANUARY;
		var season = switch (month) {
			case JANUARY, FEBRUARY, MARCH -> "Winter";
			case APRIL, MAY, JUNE -> "Spring";
			case JULY, AUGUST, SEPTEMBER -> "Summer";
			case OCTOBER, NOVEMBER, DECEMBER -> "Autumn";
			default -> throw new RuntimeException("Invalid month");
		};
		System.out.println(season);
	}
}


enum Month {
	JANUARY,
	FEBRUARY,
	MARCH,
	APRIL,
	MAY,
	JUNE,
	JULY,
	AUGUST,
	SEPTEMBER,
	OCTOBER,
	NOVEMBER,
	DECEMBER;
}