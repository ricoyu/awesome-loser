package com.loserico.pattern.creational.factorymethod;

/**
 * To display the parsed messages from the parsers, an abstract service class is
 * created which will be extends by service specific, i.e. parser specific, display
 * classes.
 * 
 * The above class is used to display the message fetched by the XML parser to the
 * user. The above class is an abstract class that contains two important methods.
 * The display method is used to display the message to the user. The getParser
 * method is the factory method which is implemented by the subclasses to
 * instantiate the parser object and the method is used by the display method in
 * order to parse the XML and gets the message to display.
 * 
 * @author Loser
 * @since Aug 28, 2016
 * @version
 *
 */
public interface DisplayService {

	public default void display() {
		XMLParser parser = getParser();
		String msg = parser.parse();
		System.out.println(msg);
	}

	XMLParser getParser();

}