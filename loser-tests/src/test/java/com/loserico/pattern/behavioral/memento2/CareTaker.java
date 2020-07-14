package com.loserico.pattern.behavioral.memento2;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the care taker class used to store and provide the requested
 * memento object. The class contains the saveMemento method is used to save the
 * memento object, the getMemento is used to return the request memento object and
 * the clearSavepoints method which is used to clear all the savepoints and it
 * deletes all the saved memento objects.
 * 
 * @author Loser
 * @since Aug 18, 2016
 * @version
 *
 */
public class CareTaker {
	private static final Logger log = LoggerFactory.getLogger(CareTaker.class);

	private final Map<String, Memento> savepointStorage = new HashMap<String, Memento>();

	public void saveMemento(Memento memento, String savepointName) {
		log.info("Saving state... {}", savepointName);
		savepointStorage.put(savepointName, memento);
	}

	public Memento getMemento(String savepointName) {
		log.info("Undo at ... {}", savepointName);
		return savepointStorage.get(savepointName);
	}

	public void clearSavepoints() {
		log.info("Clearing all save points...");
		savepointStorage.clear();

	}

}
