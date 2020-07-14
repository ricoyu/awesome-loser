package com.loserico.pattern.creational.abstractfactory;

/**
 * To avoid a dependency between the client code and the factories, optionally we
 * implemented a factory-producer which has a static method and is responsible to
 * provide a desired factory object to the client object.
 * 
 * @author Loser
 * @since Sep 1, 2016
 * @version
 *
 */
public final class ParserFactoryProducer {

	private ParserFactoryProducer() {
		throw new AssertionError();
	}

	public static AbstractParserFactory getFactory(String factoryType) {
		switch (factoryType) {
		case "NYFactory":
			return new NYParserFactory();
		case "TWFactory":
			return new TWParserFactory();
		}

		return null;
	}

}