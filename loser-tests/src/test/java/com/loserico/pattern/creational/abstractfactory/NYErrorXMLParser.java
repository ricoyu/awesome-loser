package com.loserico.pattern.creational.abstractfactory;

public class NYErrorXMLParser implements XMLParser {

	@Override
	public String parse() {
		System.out.println("NY Parsing error XML...");
		return "NY Error XML Message";
	}

}