package com.loserico.pattern.behavioral.memento2;

/**
 * The Memento class is used to store the state of the Originator and stored by the
 * caretaker. The class does not have any setter methods, it is only used to get
 * the state of the object.
 * 
 * @author Loser
 * @since Aug 18, 2016
 * @version
 *
 */
public class Memento {

	private double x;
	private double y;

	public Memento(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
