package com.loserico.pattern.visitor2;

/**
 * Implementing Visitor Pattern requires two important interfaces, an Element
 * interface which will contain an accept method with an argument of type Visitor.
 * This interface will be implemented by all the classes that need to allow visitors
 * to visit them.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public interface Element {

	public void accept(Visitor visitor);

}
