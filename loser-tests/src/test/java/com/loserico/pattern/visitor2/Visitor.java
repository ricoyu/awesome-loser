package com.loserico.pattern.visitor2;

/**
 * The other important interface is the Visitor interface; this interface will
 * contain visit methods with an argument of a class that implements the Element
 * interface.
 * 
 * Please also note that we have added two new methods in our HtmlTag class, the
 * getStartTag and the getEndTag as opposed to the example shown in the Composite
 * Design Pattern lesson.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public interface Visitor {

	public void visit(HtmlElement element);

	public void visit(HtmlParentElement parentElement);
}
