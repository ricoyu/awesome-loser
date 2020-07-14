package com.loserico.pattern.creational.factorymethod;

public class ErrorXMLDisplayService implements DisplayService {

	@Override
	public XMLParser getParser() {
		return new ErrorXMLParser();
	}

}