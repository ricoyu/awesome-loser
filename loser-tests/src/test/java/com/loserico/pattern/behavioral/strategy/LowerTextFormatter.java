package com.loserico.pattern.behavioral.strategy;

/**
 * The LowerTextFormatter is a concrete text formatter that implements the TextFormatter interface 
 * and the class is used to change the text into small case.
 * 
 * @author Rico Yu
 * @since 2016-10-29 10:00
 * @version 1.0
 *
 */
public class LowerTextFormatter implements TextFormatter {

	@Override
	public void format(String text) {
		System.out.println("[LowerTextFormatter]: " + text.toLowerCase());
	}

}