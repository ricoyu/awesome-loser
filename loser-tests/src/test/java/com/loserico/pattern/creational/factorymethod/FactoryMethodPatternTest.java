package com.loserico.pattern.creational.factorymethod;

/**
 * In the above class, you can clearly see that the by letting the subclasses to
 * implement the factory method creates the different instances of parsers which can
 * be used at runtime according to the need.
 * 
 * @author Loser
 * @since Aug 28, 2016
 * @version
 *
 */
public class FactoryMethodPatternTest {

	public static void main(String[] args) {
		DisplayService service = new FeedbackXMLDisplayService();
		service.display();

		service = new ErrorXMLDisplayService();
		service.display();

		service = new OrderXMLDisplayService();
		service.display();

		service = new ResponseXMLDisplayService();
		service.display();

	}

}