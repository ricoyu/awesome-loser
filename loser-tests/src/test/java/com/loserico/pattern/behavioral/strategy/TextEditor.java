package com.loserico.pattern.behavioral.strategy;

/**
 * The TextEditor class which holds a reference to the TextFormatter interface. 
 * The class contains the publishText method which forwards the text to the formatter 
 * in order to publish the text in desired format.
 *  
 * @author Rico Yu
 * @since 2016-10-29 10:01
 * @version 1.0
 *
 */
public class TextEditor {

	private final TextFormatter textFormatter;

	public TextEditor(TextFormatter textFormatter) {
		this.textFormatter = textFormatter;
	}

	public void publishText(String text) {
		textFormatter.format(text);
	}

}