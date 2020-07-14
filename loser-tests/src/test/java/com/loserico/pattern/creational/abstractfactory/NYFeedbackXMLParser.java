package com.loserico.pattern.creational.abstractfactory;

public class NYFeedbackXMLParser implements XMLParser {

	@Override
	public String parse() {
		System.out.println("NY Parsing feedback XML...");
		return "NY Feedback XML Message";
	}

}