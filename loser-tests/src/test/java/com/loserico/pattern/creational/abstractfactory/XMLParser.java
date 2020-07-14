package com.loserico.pattern.creational.abstractfactory;

/**
 * The above interface is implemented by the concrete parser classes to parse the
 * XMLs and returns the string message.
 * 
 * There are two clients and four different type of messages exchange between the
 * company and its client. So, there should be six different types of concrete XML
 * parsers that are specific to the client.
 * 
 * @author Loser
 * @since Sep 1, 2016
 * @version
 *
 */
public interface XMLParser {

	public String parse();

}