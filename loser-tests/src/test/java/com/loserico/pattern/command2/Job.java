package com.loserico.pattern.command2;

/**
 * The Job interface is the command interface, contains a single method run, which
 * is executed by a thread. Our command’s execute method is the run method which
 * will be used to execute by a thread in order to get the work done.
 * 
 * @author Loser
 * @since Aug 24, 2016
 * @version
 *
 */
public interface Job {

	public void run();
}