package com.loserico.pattern.behavioral.mediator1.colleague;

import com.loserico.pattern.behavioral.mediator1.mediator.Mediator;

/**
 * The Colleague interface has one method to set the mediator for the concrete
 * colleague’s class.
 * 
 * @author Rico Yu
 * @since 2017-01-09 21:33
 * @version 1.0
 *
 */
public interface Colleague {

	public void setMediator(Mediator mediator);

}