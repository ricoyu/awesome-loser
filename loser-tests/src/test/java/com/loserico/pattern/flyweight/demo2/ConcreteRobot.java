package com.loserico.pattern.flyweight.demo2;

/**
* A 'ConcreteFlyweight' class-SmallRobot
*
*/
public class ConcreteRobot implements Robot {
	String robotType;
	public String colorOfRobot;

	public ConcreteRobot(String robotType) {
		this.robotType = robotType;
	}

	public void setColor(String colorOfRobot) {
		this.colorOfRobot = colorOfRobot;
	}

	@Override
	public void print() {
		System.out.println(" This is a " + robotType + " type robot with " + colorOfRobot + " color");
	}
}