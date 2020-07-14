package com.loserico.pattern.creational.builder2;

/**
 * In the above class, we have first created a SedanCarBuilder and a CarDirector.
 * Then we ask the CarDirector to build the car for us according to the builder
 * passed to it. Then finally, we directly ask the builder to provide us the created
 * car object.
 * 
 * @author Loser
 * @since Sep 14, 2016
 * @version
 *
 */
public class BuilderPatternTest {

	public static void main(String[] args) {
		CarBuilder carBuilder = new SedanCarBuilder();
		CarDirector director = new CarDirector(carBuilder);
		director.build();
		Car car = carBuilder.getCar();
		System.out.println(car);
		System.out.println("\n");

		carBuilder = new SportsCarBuilder();
		director = new CarDirector(carBuilder);
		director.build();
		car = carBuilder.getCar();
		System.out.println(car);
	}

}
