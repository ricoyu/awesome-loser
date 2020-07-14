package com.loserico.pattern.structural.composite2;

import java.util.ArrayList;
import java.util.List;

/**
 * The HtmlParentElement class is the composite class which implements methods like
 * addChildTag, removeChildTag, getChildren which must be implemented by a class to
 * become the composite of the structure. The operation method here is the
 * generateHtml, which prints the tag of the current class, and also iterates
 * through its children and calls their generateHtml method too.
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
}