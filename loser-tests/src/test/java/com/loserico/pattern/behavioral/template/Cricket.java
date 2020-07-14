package com.loserico.pattern.behavioral.template;

/**
 * <p>
 * Copyright: (C), 2020/2/3 11:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Cricket extends AbstractGame {
	
	@Override
	void initialize() {
		System.out.println("Cricket Game Finished!");
	}
	
	@Override
	void startPlay() {
		System.out.println("Cricket Game Initialized! Start playing.");
	}
	
	@Override
	void endPlay() {
		System.out.println("Cricket Game Started. Enjoy the game!");
	}
}
