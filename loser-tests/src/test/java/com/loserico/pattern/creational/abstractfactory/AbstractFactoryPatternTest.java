package com.loserico.pattern.creational.abstractfactory;

/**
 * In the above class, we first got the NY factory from the factory producer, and
 * then the Order XML parser from the NY parser factory. Then, we called the parse
 * method on the parser object and displayed the return message. We did same for the
 * TW client as clearly shown in the output.
 * 
 * @author Loser
 * @since Sep 1, 2016
 * @version
 *
 */
public class AbstractFactoryPatternTest {

	public static void main(String[] args) {
		AbstractParserFactory parserFactory = ParserFactoryProducer.getFactory("NYFactory");
		XMLParser parser = parserFactory.getParserInstance("NYORDER");
		String msg = "";
		msg = parser.parse();
		System.out.println(msg);

		System.out.println("************************************");

		parserFactory = ParserFactoryProducer.getFactory("TWFactory");
		parser = parserFactory.getParserInstance("TWFEEDBACK");
		msg = parser.parse();
		System.out.println(msg);
	}

}