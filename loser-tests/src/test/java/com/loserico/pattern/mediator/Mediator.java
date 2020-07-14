package com.loserico.pattern.mediator;

/**
 * 抽象中介者
 * 
 * <p>
 * Copyright: (C), 2020/1/30 9:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface Mediator {
	
	public void registerRunway(Runway runway);
	
	public void registerFlight(Flight flight);
	
	public boolean isLandingOk();
	
	public void setLandingStatus(boolean status);
}
