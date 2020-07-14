package com.loserico.pattern.creational.factorymethod;

public class OrderXMLDisplayService implements DisplayService {

	@Override
	public XMLParser getParser() {
		return new OrderXMLParser();
	}

}