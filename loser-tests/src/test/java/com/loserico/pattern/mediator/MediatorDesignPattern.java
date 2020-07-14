package com.loserico.pattern.mediator;

/**
 * Air traffic controller is a great example of mediator pattern where the airport control room works as 
 * a mediator for communication between different flights. Mediator works as a router between objects and 
 * it can have it’s own logic to provide way of communication.
 * 
 * <p>
 * Copyright: (C), 2020/1/30 9:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MediatorDesignPattern {
	
	public static void main(String[] args) {
		Mediator mediator = new ATCMediator();
		
		Flight sparrow101 = new Flight(mediator);
		Runway mainRunway = new Runway(mediator);
		
		mediator.registerFlight(sparrow101);
		mediator.registerRunway(mainRunway);
		
		sparrow101.getReady();
		mainRunway.land();
		sparrow101.land();
	}
}
