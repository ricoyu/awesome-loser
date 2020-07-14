package com.loserico.pattern.behavioral.strategy;

/**
 * CapTextFormatter, is a concrete text formatter that implements the TextFormatter interface 
 * and the class is used to change the text into capital case.
 * 
 * @author Rico Yu
 * @since 2016-10-29 09:57
 * @version 1.0
 *
 */
public class CapTextFormatter implements TextFormatter {

	@Override
	public void format(String text) {
		System.out.println("[CapTextFormatter]: " + text.toUpperCase());
	}

}