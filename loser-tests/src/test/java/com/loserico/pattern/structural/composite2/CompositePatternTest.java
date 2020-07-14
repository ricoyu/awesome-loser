package com.loserico.pattern.structural.composite2;

/**
 * In this example, first we have created a parent tag (<html>) then we add a child
 * to it, which is another of composite type (<body>), and this object contains two
 * children (<p>).
 * 
 * Please note that, the above structure represents as a part-whole hierarchy and
 * the call to generateHtml() method on the parent tag allows the client to treat
 * the compositions of objects uniformly. As it generates the html of the object and
 * of all its children.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public class CompositePatternTest {

	public static void main(String[] args) {
		HtmlTag htmlTag = new HtmlParentElement("<html>");
		htmlTag.setStartTag("<html>");
		htmlTag.setEndTag("</html>");

		HtmlTag bodyTag = new HtmlParentElement("<body>");
		bodyTag.setStartTag("<body>");
		bodyTag.setEndTag("</body>");
		htmlTag.addChildTag(bodyTag);

		HtmlTag pTag = new HtmlElement("<p>");
		pTag.setStartTag("<p>");
		pTag.setEndTag("</p>");
		pTag.setTagBody("Testing html tag library");
		bodyTag.addChildTag(pTag);

		pTag = new HtmlElement("<p>");
		pTag.setStartTag("<p>");
		pTag.setEndTag("</p>");
		pTag.setTagBody("Paragraph 2");
		bodyTag.addChildTag(pTag);

		htmlTag.generateHtml();
	}
}