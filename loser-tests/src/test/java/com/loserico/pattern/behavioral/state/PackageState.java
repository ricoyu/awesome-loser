package com.loserico.pattern.behavioral.state;

/**
 * In this example, we are simulating courier delivery system where packages can be in different states during transitions.
 * <p>
 * Copyright: (C), 2020/1/31 11:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface PackageState {
	
	void updateState(DeliveryContext ctx);
}
