package com.loserico.pattern.state2;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Let’s put them all together.
 * 
 * <p>
 * Copyright: Copyright (c) 2018-12-25 13:37
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StateMain {

	public static void main(String[] args) {

		StateUIContext stateUIContext = new StateUIContext();

		try (PrintWriter printWriter = new PrintWriter(System.out)) {
			stateUIContext.setGreetingState(new AnonymousGreetingState());
			stateUIContext.create(printWriter);
			printWriter.write("\n");
			stateUIContext.setGreetingState(new LoggedInGreetingState("someone"));
			stateUIContext.create(printWriter);
			printWriter.write("\n");
			stateUIContext.setGreetingState(new AdminGreetingState("admin", new Date()));
			stateUIContext.create(printWriter);
			printWriter.write("\n");
		}
	}
}