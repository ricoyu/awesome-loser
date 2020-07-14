package com.loserico.pattern.creational.factorymethod;

public class ResponseXMLDisplayService implements DisplayService {

	@Override
	public XMLParser getParser() {
		return new ResponseXMLParser();
	}

}