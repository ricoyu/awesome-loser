package com.loserico.pattern.visitor2;

import java.util.List;

/**
 * Implementing Visitor Pattern requires two important interfaces, an Element
 * interface which will contain an accept method with an argument of type Visitor.
 * This interface will be implemented by all the classes that need to allow visitors
 * to visit them. In our case, the HtmlTag will implement the Element interface, as
 * the HtmlTag is the parent abstract class of all the concrete html classes, the
 * concrete classes will inherit and will override the accept method of the Element
 * interface.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public interface HtmlTag extends Element {

	public String getTagName();

	public void setStartTag(String tag);

	public String getStartTag();

	public String getEndTag();

	public void setEndTag(String tag);

	public default void setTagBody(String tagBody) {
		throw new UnsupportedOperationException("Current operation is not support for this object");
	}

	public default void addChildTag(HtmlTag htmlTag) {
		throw new UnsupportedOperationException("Current operation is not support for this object");
	}

	public default void removeChildTag(HtmlTag htmlTag) {
		throw new UnsupportedOperationException("Current operation is not support for this object");
	}

	public default List<HtmlTag> getChildren() {
		throw new UnsupportedOperationException("Current operation is not support for this object");
	}

	public void generateHtml();

}
