package com.loserico.pattern.behavioral.template;

/**
 * <p>
 * Copyright: (C), 2020/2/3 11:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TemplatePatternDemo {
	
	public static void main(String[] args) {
		AbstractGame game = new Cricket();
		game.play();
		
		System.out.println();
		
		game = new Football();
		game.play();
	}
}
