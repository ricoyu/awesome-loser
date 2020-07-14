package com.loserico.pattern.state2;

/**
 * The state pattern deals with altering an object’s behaviour when its state
 * changes.
 * 
 * Imagine the case of a class responsible for generating user interface based
 * on the state. You got anonymous, logged-in and admin users.
 * 
 * We shall create an interface called GreetingState which defines the action of
 * drawing a html text with a welcome message to the user. There is going to be
 * a different implementation according to the states that we have.
 * 
 * <p>
 * Copyright: Copyright (c) 2018-12-25 13:32
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface GreetingState {

	String create();

}