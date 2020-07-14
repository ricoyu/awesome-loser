package com.loserico.pattern.visitor2;

/**
 * Now, the concrete visitor classes: we have created two concrete classes, one will
 * add a css class visitor to all html tags and the other one will change the width
 * of the tag using the style attribute of the html tag.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public class StyleVisitor implements Visitor {

	@Override
	public void visit(HtmlElement element) {
		element.setStartTag(element.getStartTag().replace(">", " style='width:46px;'>"));
	}

	@Override
	public void visit(HtmlParentElement parentElement) {
		parentElement.setStartTag(parentElement.getStartTag().replace(">", " style='width:58px;'>"));
	}

}
