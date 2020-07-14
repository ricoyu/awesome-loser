package com.loserico.pattern.state2;

/**
 * We shall implement the GreetingState for the anonymous user.
 * 
 * <p>
 * Copyright: Copyright (c) 2018-12-25 13:33
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class AnonymousGreetingState implements GreetingState {

	private static final String FOOTER_MESSAGE = "<p><Hello anonymous user!</p>";

	@Override
	public String create() {
		return FOOTER_MESSAGE;
	}

}