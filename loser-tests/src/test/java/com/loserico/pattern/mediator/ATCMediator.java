package com.loserico.pattern.mediator;

/**
 * <p>
 * Copyright: (C), 2020/1/30 9:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ATCMediator implements Mediator {
	
	private Flight flight;
	
	private Runway runway;
	
	public boolean land;
	
	@Override
	public void registerRunway(Runway runway) {
		this.runway = runway;
	}
	
	@Override
	public void registerFlight(Flight flight) {
		this.flight = flight;
	}
	
	@Override
	public boolean isLandingOk() {
		return land;
	}
	
	@Override
	public void setLandingStatus(boolean status) {
		this.land = status;
	}
}
