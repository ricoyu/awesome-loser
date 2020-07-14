package com.loserico.pattern.creational.factorymethod;

/**
 * The ErrorXMLParser implements the XMLParser and is used to parse the error
 * message XMLs.
 * 
 * @author Loser
 * @since Aug 28, 2016
 * @version
 *
 */
public class ErrorXMLParser implements XMLParser {

	@Override
	public String parse() {
		System.out.println("Parsing error XML...");
		return "Error XML Message";
	}

}