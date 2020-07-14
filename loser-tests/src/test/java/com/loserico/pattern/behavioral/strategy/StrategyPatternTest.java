package com.loserico.pattern.behavioral.strategy;

/**
 * In the above class, we have first created a CapTextFormatter and assigned it to the 
 * TextEditor instance. Then we called the publishText method and passed some input text to it.
 * 
 * Again, we did the same thing, but this time, the LowerTextFormatter is passed to the TextEditor.
 * 
 * The output clearly shows the different text format produced by the different text editors due 
 * to the different text formatter used by it.
 * 
 * The main advantage of the Strategy Design Pattern is that we can enhance the code without 
 * much trouble. We can add new text formatters without disturbing the current code. 
 * This would make our code maintainable and flexible. 
 * 
 * This design pattern also promotes the “code to interface” design principle.

 * @author Rico Yu
 * @since 2016-10-29 10:03
 * @version 1.0
 *
 */
public class StrategyPatternTest {

	public static void main(String[] args) {
		TextFormatter formatter = new CapTextFormatter();
		TextEditor editor = new TextEditor(formatter);
		editor.publishText("Testing text in caps formatter");

		formatter = new LowerTextFormatter();
		editor = new TextEditor(formatter);
		editor.publishText("Testing text in lower formatter");

	}

}