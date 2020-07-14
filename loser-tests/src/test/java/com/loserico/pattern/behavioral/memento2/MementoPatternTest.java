package com.loserico.pattern.behavioral.memento2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MementoPatternTest {
	private static final Logger log = LoggerFactory.getLogger(MementoPatternTest.class);

	public static void main(String[] args) {
		CareTaker careTaker = new CareTaker();
		Originator originator = new Originator(5, 7, careTaker);
		log.info("Default State: {}", originator);

		originator.setX(originator.getY() * 51);
		log.info("State: {}", originator);
		originator.createSavepoint("SAVE1");

		originator.setY(originator.getX() / 22);
		log.info("State: {}", originator);

		originator.undo();
		log.info("State after undo: {}", originator);

		originator.setX(Math.pow(originator.getX(), 3));
		originator.createSavepoint("SAVE2");
		log.info("State: {}", originator);

		originator.setY(originator.getX() - 30);
		originator.createSavepoint("SAVE3");
		log.info("State: {}", originator);

		originator.setY(originator.getX() / 22);
		originator.createSavepoint("SAVE4");
		log.info("State: {}", originator);

		originator.undo("SAVE2");
		log.info("Retrieving at: {}", originator);

		originator.undoAll();
		log.info("State after undo all: {}", originator);
	}
}
