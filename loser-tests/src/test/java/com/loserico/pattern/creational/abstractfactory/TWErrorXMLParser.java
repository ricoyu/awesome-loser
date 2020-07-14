package com.loserico.pattern.creational.abstractfactory;

public class TWErrorXMLParser implements XMLParser {

	@Override
	public String parse() {
		System.out.println("TW Parsing error XML...");
		return "TW Error XML Message";
	}

}