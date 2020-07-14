package com.loserico.pattern.creational.builder2;

/**
 * The CarBuilder is the builder interface contains set of common methods used to
 * build the car object and its components.
 * 
 * @author Loser
 * @since Sep 14, 2016
 * @version
 *
 */
public interface CarBuilder {

	public void buildBodyStyle();

	public void buildPower();

	public void buildEngine();

	public void buildBreaks();

	public void buildSeats();

	public void buildWindows();

	public void buildFuelType();

	/**
	 * The getCar method is used to return the final car object to the client after
	 * its construction.
	 * 
	 * @return
	 */
	public Car getCar();
}