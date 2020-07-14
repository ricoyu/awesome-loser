package com.loserico.pattern.creational.factorymethod;

public class ResponseXMLParser implements XMLParser {

	@Override
	public String parse() {
		System.out.println("Parsing response XML...");
		return "Response XML Message";
	}

}