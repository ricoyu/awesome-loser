package com.loserico.pattern.creational.abstractfactory;

/**
 * To implement the Abstract Factory Design Pattern will we first create an
 * interface that will be implemented by all the concrete factories.
 * 
 * This interface is implemented by the client specific concrete factories
 * which will provide the XML parser object to the client object. The
 * getParserInstance method takes the parserType as an argument which is used to get
 * the message specific (error parser, order parser etc) parser object.
 * 
 * @author Loser
 * @since Sep 1, 2016
 * @version
 *
 */
public interface AbstractParserFactory {

	public XMLParser getParserInstance(String parserType);
}