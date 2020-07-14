package com.loserico.pattern.creational.factorymethod;

public class FeedbackXMLDisplayService implements DisplayService {

	@Override
	public XMLParser getParser() {
		return new FeedbackXML();
	}

}