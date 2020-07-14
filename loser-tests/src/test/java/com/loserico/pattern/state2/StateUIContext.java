package com.loserico.pattern.state2;

import java.io.PrintWriter;

/**
 * The we shall create the stateui context.
 * 
 * <p>
 * Copyright: Copyright (c) 2018-12-25 13:36
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StateUIContext {

	private GreetingState greetingState;

	public void setGreetingState(GreetingState greetingState) {
		this.greetingState = greetingState;
	}

	public void create(PrintWriter printWriter) {
		printWriter.write(greetingState.create());
	}
}