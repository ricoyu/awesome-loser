package com.loserico.pattern.creational.factorymethod;

/**
 * To parse the feedback message XMLs.
 * @author Loser
 * @since Aug 28, 2016
 * @version 
 *
 */
public class FeedbackXML implements XMLParser {

	@Override
	public String parse() {
		System.out.println("Parsing feedback XML...");
		return "Feedback XML Message";
	}

}