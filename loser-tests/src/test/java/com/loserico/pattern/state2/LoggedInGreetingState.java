package com.loserico.pattern.state2;

/**
 * Then we shall implement the GreetingState for the logged in user. This one would create a personalised message.
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
public class LoggedInGreetingState implements GreetingState {

	private static final String FOOTER_MESSAGE = "<p><Hello %s!</p>";

	private final String username;

	public LoggedInGreetingState(final String username) {
		this.username = username;
	}

	@Override
	public String create() {
		return String.format(FOOTER_MESSAGE, username);
	}

}