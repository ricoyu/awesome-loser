package com.loserico.pattern.flyweight.demo2;

import java.util.HashMap;
import java.util.Map;

/**
* The 'FlyweightFactory' class
*
*/
public class RobotFactory {
	Map<String, Robot> shapes = new HashMap<String, Robot>();

	public int TotalObjectsCreated() {
		return shapes.size();
	}

	public Robot getRobotFromFactory(String robotType) throws Exception {
		Robot robotCategory = null;
		if (shapes.containsKey(robotType)) {
			robotCategory = shapes.get(robotType);
		} else {
			switch (robotType) {
			case "King":
				System.out.println("We do not have King Robot. So we are creating a King Robot now.");
				robotCategory = new ConcreteRobot("King");
				shapes.put("King", robotCategory);
				break;
			case "Queen":
				System.out.println("We do not have Queen Robot. So we are creating a Queen Robot now.");
				robotCategory = new ConcreteRobot("Queen");
				shapes.put("Queen", robotCategory);
				break;
			default:
				throw new Exception(" Robot Factory can create only king and queen type robots");
			}
		}
		return robotCategory;
	}
}