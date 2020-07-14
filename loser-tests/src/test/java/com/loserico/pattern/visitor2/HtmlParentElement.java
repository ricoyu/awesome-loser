package com.loserico.pattern.visitor2;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface HtmlTag class extends the Element interface.
 * 
 * The below concrete classes will override the accept method of the Element
 * interface and will call the visit method, and will pass this operator as an
 * argument. This will allow the visitor method to get all the public members of the
 * object, to add new operations on it.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public class HtmlParentElement implements HtmlTag {

	private String tagName;
	private String startTag;
	private String endTag;
	private List<HtmlTag> childrenTag;

	public HtmlParentElement(String tagName) {
		this.tagName = tagName;
		this.startTag = "";
		this.endTag = "";
		this.childrenTag = new ArrayList<>();
	}

	@Override
	public String getTagName() {
		return tagName;
	}

	@Override
	public void setStartTag(String tag) {
		this.startTag = tag;
	}

	@Override
	public void setEndTag(String tag) {
		this.endTag = tag;
	}

	@Override
	public String getStartTag() {
		return startTag;
	}

	@Override
	public String getEndTag() {
		return endTag;
	}

	@Override
	public void addChildTag(HtmlTag htmlTag) {
		childrenTag.add(htmlTag);
	}

	@Override
	public void removeChildTag(HtmlTag htmlTag) {
		childrenTag.remove(htmlTag);
	}

	@Override
	public List<HtmlTag> getChildren() {
		return childrenTag;
	}

	@Override
	public void generateHtml() {
		System.out.println(startTag);
		for (HtmlTag tag : childrenTag) {
			tag.generateHtml();
		}
		System.out.println(endTag);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
