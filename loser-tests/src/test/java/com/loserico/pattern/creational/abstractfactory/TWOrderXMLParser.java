package com.loserico.pattern.creational.abstractfactory;

public class TWOrderXMLParser implements XMLParser {

	@Override
	public String parse() {
		System.out.println("TW Parsing order XML...");
		return "TW Order XML Message";
	}

}