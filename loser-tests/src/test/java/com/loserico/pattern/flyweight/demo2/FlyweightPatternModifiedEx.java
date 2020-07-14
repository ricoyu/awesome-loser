package com.loserico.pattern.flyweight.demo2;

import java.util.Random;

/**
* @author sarcarv
*FlyweightPattern is in action.
*/
public class FlyweightPatternModifiedEx {
	public static void main(String[] args) throws Exception {
		RobotFactory myfactory = new RobotFactory();
		ConcreteRobot shape;
		/*Here we are trying to get 3 king type robots*/
		for (int i = 0; i < 3; i++) {
			shape = (ConcreteRobot) myfactory.getRobotFromFactory("King");
			shape.setColor(getRandomColor());
			shape.print();
		}
		/*Here we are trying to get 3 queen type robots*/
		for (int i = 0; i < 3; i++) {
			shape = (ConcreteRobot) myfactory.getRobotFromFactory("Queen");
			shape.setColor(getRandomColor());
			shape.print();
		}
		int NumOfDistinctRobots = myfactory.TotalObjectsCreated();
		//System.out.println("\nDistinct Robot objects created till now ="+ NumOfDistinctRobots);
		System.out.println("\n Finally no of Distinct Robot objects created: " + NumOfDistinctRobots);
	}

	static String getRandomColor() {
		Random r = new Random();
		/*You can supply any number of your choice in nextInt argument.* we are simply checking the random number generated is an even number
		* or an odd number. And based on that we are choosing the color. For
		simplicity, we'll use only two colors—red and green
		*/
		int random = r.nextInt(20);
		if (random % 2 == 0) {
			return "red";
		} else {
			return "green";
		}
	}
}