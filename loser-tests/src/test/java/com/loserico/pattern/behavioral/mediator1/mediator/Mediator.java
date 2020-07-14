package com.loserico.pattern.behavioral.mediator1.mediator;

/**
 * The MachineMediator is an interface which acts as a generic mediator. The interface
 * contains operations call by one object to another.
 * 
 * @author Rico Yu
 * @since 2017-01-09 21:32
 * @version 1.0
 *
 */
public interface Mediator {

	public void start();

	public void wash();

	public void open();

	public void closed();

	public void on();

	public void off();

	public boolean checkTemperature(int temp);

}
