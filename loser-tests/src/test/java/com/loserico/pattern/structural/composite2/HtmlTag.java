package com.loserico.pattern.structural.composite2;

import java.util.List;

/**
 * The HtmlTag class is the component class which defines all the methods used by
 * the composite and the leaf class. There are some methods which should be common
 * in both the extended classes; hence these methods are kept abstract in the above
 * class, to enforce their implementation in the child classes.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public interface HtmlTag {

	/*
	 * The getTagName(), just returns the tag name and should be used by both child
	 * classes, i.e., the composite class and the leaf class.
	 */
	public String getTagName();

	/*
	 * Every html element should have a start tag and an end tag, the methods
	 * setStartTag and setEndTag are used to set the start and end tag of an html
	 * element and should be implemented by both the child classes
	 */
	public void setStartTag(String tag);

	public void setEndTag(String tag);

	public void generateHtml();

	/*
	 * There are methods which are useful only to the composite class and are
	 * useless to the leaf class. Just provide the default implementation to these
	 * methods, throwing an exception is a good implementation of these methods to
	 * avoid any accidental call to these methods by the object which should not
	 * support them.
	 */
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

}